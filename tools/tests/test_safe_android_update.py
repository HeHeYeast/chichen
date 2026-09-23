"""Behavioral updater tests: real backup parser and disk files, simulated ADB only."""
import argparse
from contextlib import redirect_stdout
import copy
import hashlib
import importlib.util
import io
import json
from pathlib import Path
import subprocess
import tempfile
import time
import unittest
from datetime import datetime, timezone

ROOT = Path(__file__).resolve().parents[2]
spec = importlib.util.spec_from_file_location('safe_update', ROOT / 'tools/safe_android_update.py')
update = importlib.util.module_from_spec(spec); spec.loader.exec_module(update)


class Phone:
    def __init__(self, args, state):
        self.args, self.state = args, state
        self.code, self.name = 14, '1.4.7'
        self.devices = 'phone-A device model:Test'
        self.calls = []
        self.snapshots = 0
        self.installed = False
        self.invalid_backup = None
        self.mutate_before = False
        self.mutate_after = False
        self.install_result = b'Success\n'
        self.first_changed = False
        self.exists = True
        self.fail_after = False
        self.stale = False
        self.user = '0'
        self.launch_result = b'Starting: Intent\nStatus: ok\n'
        self.pid = b'1234'

    def backup(self):
        version = '1.4.8' if self.installed else self.name
        state = copy.deepcopy(self.state)
        if (self.installed and self.mutate_after) or (self.snapshots > 1 and not self.installed and self.mutate_before):
            state['cp'] += 1
        raw = json.dumps(state, ensure_ascii=False)
        now = time.time() - (601 if self.stale else 0)
        return json.dumps({'format': 'chick-kitchen', 'formatVersion': 1,
                          'exportedAt': datetime.fromtimestamp(now, timezone.utc).isoformat(), 'gameVersion': version,
                          'save': state, 'updateBackup': {'protocol': 1, 'applicationId': update.PACKAGE,
                          'versionCode': 15 if self.installed else self.code,
                          'sourceSha256': hashlib.sha256(raw.encode()).hexdigest()}}, ensure_ascii=False).encode()

    def run(self, args, timeout=40):
        args = [str(a) for a in args]
        if args[0] == 'node':
            return update.run_command(args, timeout)
        self.calls.append(args)
        command = args[3:] if args[1:2] == ['-s'] else args[1:]
        if command == ['devices', '-l']:
            return ('List of devices attached\n' + self.devices + '\n').encode()
        if command[:3] == ['shell', 'am', 'get-current-user']:
            return self.user.encode()
        if command[:4] == ['shell', 'pm', 'list', 'packages']:
            return (f'package:{update.PACKAGE}' if self.exists or self.installed else '').encode()
        if command[:3] == ['shell', 'dumpsys', 'package']:
            first = '2026-09-20' if self.first_changed and self.installed else '2026-09-14'
            code, name = (15, '1.4.8') if self.installed else (self.code, self.name)
            return f'versionCode={code}\nversionName={name}\nfirstInstallTime={first} 07:57:06\n'.encode()
        if command[:3] == ['shell', 'am', 'force-stop']:
            return b''
        if command[:3] == ['exec-out', 'content', 'read']:
            self.snapshots += 1
            if self.installed and self.fail_after:
                raise update.UpdateError('device disconnected')
            return self.invalid_backup if self.invalid_backup is not None else self.backup()
        if command[:1] == ['install']:
            # Assert the safety property at the external side-effect boundary.
            if self.exists:
                backups = list(self.args.backup_root.glob('*/before-update.json'))
                if len(backups) != 1:
                    raise AssertionError('Install reached without a saved backup')
                parsed = json.loads(update.run_command(['node', ROOT / 'tools/check-update-backup.mjs', backups[0]]))
                if parsed['summary']['cp'] != self.state['cp']:
                    raise AssertionError('Wrong progress backed up')
            if self.install_result == b'Success\n':
                self.installed = True
            return self.install_result
        if command[:3] == ['shell', 'am', 'start']:
            return self.launch_result
        if command[:2] == ['shell', 'pidof']:
            return self.pid
        raise AssertionError(command)


class SafeUpdateTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        raw = update.run_command(['node', '--input-type=module', '-e',
            "import {freshState} from './web/engine.js'; console.log(JSON.stringify(freshState(Date.now()))));".replace('))));', ')));')])
        cls.state = json.loads(raw)

    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix='jibao-update-tests-')
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        apk = self.root / 'test.apk'; apk.write_bytes(b'FAKE-APK-FOR-TEST-ONLY')
        identity = update.read_json(ROOT / 'android/release-identity.json')
        apk.with_suffix('.release.json').write_text(json.dumps({'applicationId': update.PACKAGE,
            'versionCode': 15, 'versionName': '1.4.8', 'sha256': update.sha256(apk),
            'certificateSha256': identity['certificateSha256']}), encoding='utf-8')
        self.args = argparse.Namespace(apk=apk, adb='fake-adb', serial='', backup_root=self.root/'backups',
            legacy_backup=None, check_only=False, backup_only=False, launch=True)
        self.phone = Phone(self.args, self.state)

    def run_update(self):
        with redirect_stdout(io.StringIO()):
            return update.Updater(self.args, self.phone.run).perform()

    def stopped(self, pattern):
        with self.assertRaisesRegex((update.UpdateError, ValueError), pattern):
            self.run_update()
        self.assertFalse(self.phone.installed)

    def test_success_keeps_both_snapshots_and_launches_last(self):
        report = self.run_update()
        self.assertTrue(report['saveVerified']); self.assertEqual(report['status'], 'complete')
        self.assertEqual(len(list(self.args.backup_root.glob('*/before-update.json'))), 1)
        self.assertEqual(len(list(self.args.backup_root.glob('*/after-update.json'))), 1)
        self.assertIn('pidof', self.phone.calls[-1])
        self.assertTrue(report['nativeLaunchVerified']); self.assertFalse(report['webUiVerified'])
        self.assertTrue(all('uninstall' not in c and 'clear' not in c and '-d' not in c for c in self.phone.calls))

    def test_corrupt_apk_never_contacts_phone(self):
        self.args.apk.write_bytes(b'changed'); self.stopped('身份校验失败'); self.assertEqual(self.phone.calls, [])

    def test_unauthorized_device_stops(self):
        self.phone.devices = 'phone-A unauthorized'; self.stopped('USB')

    def test_multiple_devices_require_choice(self):
        self.phone.devices += '\nphone-B device'; self.stopped('多台')

    def test_explicit_device_choice(self):
        self.phone.devices += '\nphone-B device'; self.args.serial = 'phone-B'
        self.assertEqual(self.run_update()['serial'], 'phone-B')

    def test_downgrade_stops_before_force_stop(self):
        self.phone.code = 16; self.stopped('降级'); self.assertFalse(any('force-stop' in c for c in self.phone.calls))

    def test_non_primary_profile_stops(self):
        self.phone.user = '10'; self.stopped('主用户')

    def test_read_only_check_does_not_stop_install_or_back_up(self):
        self.args.check_only = True
        self.assertEqual(self.run_update()['status'], 'checked')
        self.assertFalse(self.args.backup_root.exists())
        self.assertFalse(any('force-stop' in c or 'install' in c or 'exec-out' in c for c in self.phone.calls))

    def test_provider_error_with_zero_exit_does_not_install(self):
        self.phone.invalid_backup = b'Error while accessing provider'; self.stopped('完整备份')

    def test_truncated_json_does_not_install(self):
        self.phone.invalid_backup = b'{"format":'; self.stopped('工具执行失败')

    def test_invalid_save_does_not_install(self):
        data = json.loads(self.phone.backup()); data['save']['cp'] = -1
        self.phone.invalid_backup = json.dumps(data).encode(); self.stopped('工具执行失败')

    def test_stale_snapshot_stops(self):
        self.phone.stale = True; self.stopped('十分钟')

    def test_backup_changes_before_install_stops(self):
        self.phone.mutate_before = True; self.stopped('进度发生变化')

    def test_backup_only_never_installs(self):
        self.args.backup_only = True
        self.assertEqual(self.run_update()['status'], 'backup-complete'); self.assertFalse(self.phone.installed)

    def test_backup_only_works_without_release_apk(self):
        self.args.backup_only = True; self.args.apk.unlink()
        self.assertEqual(self.run_update()['status'], 'backup-complete')
        self.assertFalse(self.phone.installed)

    def test_launch_failure_keeps_verified_save(self):
        self.phone.launch_result = b'Error: unavailable'
        with self.assertRaisesRegex(update.UpdateError, '自动启动失败'):
            self.run_update()
        report = update.read_json(next(self.args.backup_root.glob('*/report.json')))
        self.assertTrue(report['installed']); self.assertTrue(report['saveVerified'])

    def test_missing_process_does_not_claim_startup(self):
        self.phone.pid = b''
        with self.assertRaisesRegex(update.UpdateError, '进程未运行'):
            self.run_update()
        self.assertTrue(self.phone.installed)

    def test_legacy_requires_fresh_export_before_phone_is_touched(self):
        self.phone.code, self.phone.name = 13, '1.4.6'; self.stopped('首次从')
        self.assertFalse(any('force-stop' in c for c in self.phone.calls))

    def test_one_time_legacy_bootstrap(self):
        self.phone.code, self.phone.name = 13, '1.4.6'
        data = json.loads(self.phone.backup()); del data['updateBackup']
        self.args.legacy_backup = self.root/'legacy.json'
        self.args.legacy_backup.write_text(json.dumps(data), encoding='utf-8')
        self.assertTrue(self.run_update()['saveVerified'])

    def test_install_failure_keeps_backup(self):
        self.phone.install_result = b'Failure [INSTALL_FAILED_TEST]'; self.stopped('未确认安装')
        self.assertEqual(len(list(self.args.backup_root.glob('*/before-update.json'))), 1)

    def test_post_update_difference_keeps_both_backups_and_never_launches(self):
        self.phone.mutate_after = True
        with self.assertRaisesRegex(update.UpdateError, '存档有差异'):
            self.run_update()
        report = update.read_json(next(self.args.backup_root.glob('*/report.json')))
        self.assertTrue(report['installed']); self.assertFalse(report['saveVerified'])
        self.assertEqual(len(list(self.args.backup_root.glob('*/after-update.json'))), 1)
        self.assertFalse(any('start' in c for c in self.phone.calls))

    def test_post_install_disconnect_reports_installed_but_unverified(self):
        self.phone.fail_after = True
        with self.assertRaisesRegex(update.UpdateError, 'disconnected'):
            self.run_update()
        report = update.read_json(next(self.args.backup_root.glob('*/report.json')))
        self.assertTrue(report['installed']); self.assertFalse(report['saveVerified'])

    def test_changed_first_install_time_is_rejected(self):
        self.phone.first_changed = True
        with self.assertRaisesRegex(update.UpdateError, '首次安装时间'):
            self.run_update()

    def test_first_install_is_explicit(self):
        self.phone.exists = False
        report = self.run_update()
        self.assertTrue(report['installed']); self.assertFalse(report['saveVerified'])


if __name__ == '__main__':
    unittest.main()
