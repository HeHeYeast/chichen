"""Back up, validate, replace, and verify Jibao Kitchen without UI clicking."""
from __future__ import annotations
import argparse
from contextlib import contextmanager
from datetime import datetime, timezone
import hashlib
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import sys
import time
import uuid

ROOT = Path(__file__).resolve().parents[1]
PACKAGE = 'com.jibao.kitchen'
URI = 'content://com.jibao.kitchen.update-backup/snapshot'
FIRST_PROTOCOL_VERSION = 14
MAX_BACKUP = 2 * 1024 * 1024


class UpdateError(RuntimeError):
    pass


def read_json(path):
    return json.loads(Path(path).read_text(encoding='utf-8-sig'))


def write_json(path, value):
    temp = path.with_suffix('.tmp')
    temp.write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    os.replace(temp, path)


def sha256(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def run_command(args, timeout=40):
    result = subprocess.run([str(arg) for arg in args], capture_output=True, timeout=timeout,
                            env={**os.environ, 'ADB_LIBUSB': '1'})
    if result.returncode:
        detail = result.stderr.decode('utf-8', errors='replace').strip() or result.stdout.decode('utf-8', errors='replace').strip()
        raise UpdateError(f'工具执行失败：{detail[:1200]}')
    return result.stdout


@contextmanager
def device_lock(folder, serial):
    """OS releases the lock on exit/crash; there is no stale-lock override."""
    name = hashlib.sha256(serial.encode()).hexdigest()[:24]
    with (folder / f'.device-{name}.lock').open('a+b') as stream:
        stream.seek(0)
        if not stream.read(1):
            stream.write(b'0'); stream.flush()
        stream.seek(0)
        try:
            if os.name == 'nt':
                import msvcrt
                msvcrt.locking(stream.fileno(), msvcrt.LK_NBLCK, 1)
            else:
                import fcntl
                fcntl.flock(stream.fileno(), fcntl.LOCK_EX | fcntl.LOCK_NB)
        except OSError as error:
            raise UpdateError('这台手机已有更新流程正在运行。') from error
        try:
            yield
        finally:
            stream.seek(0)
            if os.name == 'nt':
                msvcrt.locking(stream.fileno(), msvcrt.LK_UNLCK, 1)
            else:
                fcntl.flock(stream.fileno(), fcntl.LOCK_UN)


class Updater:
    def __init__(self, args, run=run_command):
        self.args, self.run = args, run
        self.serial = None
        self.folder = None
        self.report = {'schema': 1, 'applicationId': PACKAGE, 'startedAt': datetime.now(timezone.utc).isoformat(),
                       'status': 'preflight', 'steps': [], 'installed': False, 'saveVerified': False}

    def step(self, name, **values):
        self.report.update(values)
        self.report['steps'].append({'step': name, 'at': datetime.now(timezone.utc).isoformat()})
        if self.folder:
            write_json(self.folder / 'report.json', self.report)
        print(name, flush=True)

    def adb(self, *args, timeout=40):
        prefix = [self.args.adb] + (['-s', self.serial] if self.serial else [])
        return self.run(prefix + list(args), timeout=timeout).decode('utf-8').strip()

    def check_backup(self, path, version, *, automatic):
        if not path.is_file() or not 0 < path.stat().st_size <= MAX_BACKUP:
            raise UpdateError('备份缺失、为空或过大，已停止安装。')
        checked = json.loads(self.run(['node', ROOT / 'tools/check-update-backup.mjs', path]).decode('utf-8'))
        if checked['gameVersion'] != version['name']:
            raise UpdateError('备份的游戏版本与手机不符，已停止安装。')
        if automatic and (checked['protocol'] != 1 or checked['versionCode'] != version['code']):
            raise UpdateError('手机没有返回受支持的自动备份，已停止安装。')
        age = time.time() * 1000 - checked['exportedAt']
        if not -120_000 <= age <= 600_000:
            raise UpdateError('备份不是刚导出的文件（需在十分钟内，手机与电脑时间需一致），已停止安装。')
        return checked

    def snapshot(self, filename, version):
        raw = self.adb('exec-out', 'content', 'read', '--uri', URI, '--user', '0')
        # ADB content can print an error with exit 0. Never accept that as a backup.
        if not raw.startswith('{') or len(raw.encode('utf-8')) > MAX_BACKUP:
            raise UpdateError('无法取得手机的完整备份，已停止后续步骤。')
        path = self.folder / filename
        path.write_text(raw, encoding='utf-8')
        result = self.check_backup(path, version, automatic=True)
        if sha256(path) != result['fileSha256']:
            raise UpdateError('电脑备份落盘校验失败。')
        return result

    def package_version(self):
        packages = self.adb('shell', 'pm', 'list', 'packages', '--user', '0', PACKAGE)
        if f'package:{PACKAGE}' not in packages.splitlines():
            if packages.strip():
                raise UpdateError('无法确定手机上的应用状态。')
            return None
        info = self.adb('shell', 'dumpsys', 'package', PACKAGE)
        code = re.search(r'\bversionCode=(\d+)', info)
        name = re.search(r'\bversionName=([^\s]+)', info)
        first = re.search(r'\bfirstInstallTime=([^\r\n]+)', info)
        if not code or not name or not first:
            raise UpdateError('无法核对已安装的版本和安装时间。')
        return {'code': int(code[1]), 'name': name[1], 'firstInstallTime': first[1].strip()}

    def prepare(self):
        if not self.args.backup_only:
            self.prepare_apk()
        self.run(['node', '--version'])
        devices = [line.split()[0] for line in self.adb('devices', '-l').splitlines() if re.match(r'^\S+\s+device\b', line)]
        serial = self.args.serial
        if (serial and serial not in devices) or (not serial and len(devices) != 1):
            raise UpdateError('请连接并允许 USB 调试；多台手机连接时需指定设备。没有安装任何内容。')
        self.serial = serial or devices[0]
        self.report['serial'] = self.serial
        if self.adb('shell', 'am', 'get-current-user') != '0':
            raise UpdateError('请在手机主用户空间更新，暂不支持分身或工作资料。')
        self.before = self.package_version()
        self.report['beforeVersion'] = self.before
        if not self.args.backup_only and self.before and self.before['code'] > self.target['code']:
            raise UpdateError('手机上的版本更新，已阻止降级。')
        self.step('设备检查通过' if self.args.backup_only else '安装包与设备检查通过')

    def prepare_apk(self):
        metadata = read_json(self.args.apk.with_suffix('.release.json'))
        identity = read_json(ROOT / 'android/release-identity.json')
        if (metadata.get('applicationId') != PACKAGE or identity['applicationId'] != PACKAGE
                or metadata.get('certificateSha256') != identity['certificateSha256']
                or metadata.get('sha256') != sha256(self.args.apk)
                or type(metadata.get('versionCode')) is not int
                or not re.fullmatch(r'\d+\.\d+\.\d+', metadata.get('versionName', ''))):
            raise UpdateError('安装包或发布身份校验失败。')
        self.target = {'code': metadata['versionCode'], 'name': metadata['versionName']}
        if self.target['code'] < FIRST_PROTOCOL_VERSION:
            raise UpdateError('自动更新工具要求目标版本为 1.4.7 或更新版本。')
        self.report.update(target=self.target, apkSha256=metadata['sha256'])

    def perform(self):
        self.prepare()
        if self.args.check_only:
            return self.report | {'status': 'checked', 'needsLegacyBackup': bool(self.before and self.before['code'] < FIRST_PROTOCOL_VERSION)}
        self.args.backup_root.mkdir(parents=True, exist_ok=True)
        with device_lock(self.args.backup_root, self.serial):
            if self.package_version() != self.before:
                raise UpdateError('手机版本在检查期间发生变化，请重新运行。')
            name = datetime.now().strftime('%Y%m%d-%H%M%S') + '-' + uuid.uuid4().hex[:8]
            self.folder = self.args.backup_root / name
            self.folder.mkdir()
            self.step('流程记录已创建')
            try:
                self.update()
            except Exception as error:
                self.step('流程已停止；保留已有备份，不回滚或清空进度', status='failed', error=str(error))
                raise
            finally:
                print(f'备份与报告：{self.folder}', flush=True)
        return self.report

    def update(self):
        before_save = None
        legacy = bool(self.before and self.before['code'] < FIRST_PROTOCOL_VERSION)
        if self.args.backup_only and not self.before:
            raise UpdateError('手机上没有已安装的游戏，无法备份。')
        if self.before:
            if legacy:
                if not self.args.legacy_backup:
                    raise UpdateError('首次从 1.4.6 或更早版本接入：请在游戏设置导出一次最新备份，使用 -LegacyBackup 文件路径；之后更新无需此步骤。')
                source = self.args.legacy_backup
                self.check_backup(source, self.before, automatic=False)
                path = self.folder / 'before-update.json'
                shutil.copyfile(source, path)
                before_save = self.check_backup(path, self.before, automatic=False)
            self.step('暂时关闭游戏，准备保存更新前快照')
            self.adb('shell', 'am', 'force-stop', '--user', '0', PACKAGE)
            if not legacy:
                before_save = self.snapshot('before-update.json', self.before)
            self.step('更新前备份已落盘并通过游戏存档校验', beforeBackup=before_save, legacyBootstrap=legacy)
        else:
            self.step('首次安装：手机上没有旧应用，无旧存档可备份')
        if self.args.backup_only:
            self.step('备份完成，没有安装任何内容', status='backup-complete')
            return
        if before_save and not legacy:
            recheck = self.snapshot('before-install-check.json', self.before)
            if recheck['sourceSha256'] != before_save['sourceSha256'] or recheck['stateSha256'] != before_save['stateSha256']:
                raise UpdateError('备份后游戏进度发生变化，已停止安装，请暂停操作手机后重试。')
        if sha256(self.args.apk) != self.report['apkSha256']:
            raise UpdateError('安装包在检查后发生变化，已停止安装。')
        self.step('备份确认完成，开始覆盖安装')
        result = self.adb('install', '-r', '--no-incremental', str(self.args.apk), timeout=240)
        (self.folder / 'install.log').write_text(result + '\n', encoding='utf-8')
        if not re.search(r'^Success\s*$', result, re.M):
            raise UpdateError('Android 未确认安装成功，请查看日志。旧数据和备份均保留。')
        self.step('覆盖安装成功，核对版本与存档', installed=True)
        after = self.package_version()
        if not after or after['code'] != self.target['code'] or after['name'] != self.target['name']:
            raise UpdateError('安装后的版本不符，已停止自动启动。')
        if self.before and after['firstInstallTime'] != self.before['firstInstallTime']:
            raise UpdateError('首次安装时间发生变化，已停止自动启动。')
        self.report['afterVersion'] = after
        if before_save:
            after_save = self.snapshot('after-update.json', after)
            self.report['afterBackup'] = after_save
            key = 'bootstrapSha256' if legacy else 'stateSha256'
            if before_save[key] != after_save[key] or (not legacy and before_save['sourceSha256'] != after_save['sourceSha256']):
                raise UpdateError('更新前后存档有差异；两份备份都已保留，未自动恢复旧档，请核对报告。')
            self.step('更新前后进度一致', saveVerified=True)
        if self.args.launch:
            result = self.adb('shell', 'am', 'start', '-W', '-a', 'android.intent.action.MAIN', '-c', 'android.intent.category.LAUNCHER', '-n', PACKAGE + '/.MainActivity')
            (self.folder / 'launch.log').write_text(result + '\n', encoding='utf-8')
            if 'Error' in result or 'Exception' in result or not re.search(r'^Status:\s*ok\s*$', result, re.M):
                raise UpdateError('安装及核对完成，但自动启动失败，请从桌面打开游戏。')
            # This verifies the native process, not rendered WebView content.
            pid = self.adb('shell', 'pidof', PACKAGE)
            if not re.fullmatch(r'\d+(?:\s+\d+)*', pid):
                raise UpdateError('启动命令已返回，但游戏进程未运行；备份保留，请检查手机。')
            self.step('原生启动检查通过；游戏画面仍需真机核对', nativeLaunchVerified=True, webUiVerified=False)
        self.step('自动更新完成', status='complete')


def arguments(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--apk', type=Path)
    parser.add_argument('--serial', default='')
    parser.add_argument('--adb', default=r'D:\gxy_code\_toolchain\android-sdk\platform-tools\adb.exe')
    parser.add_argument('--backup-root', type=Path, default=ROOT / 'artifacts/device-backups')
    parser.add_argument('--legacy-backup', type=Path)
    parser.add_argument('--launch', action='store_true')
    mode = parser.add_mutually_exclusive_group()
    mode.add_argument('--backup-only', action='store_true')
    mode.add_argument('--check-only', action='store_true')
    args = parser.parse_args(argv)
    if not args.apk:
        version = read_json(ROOT / 'android/release.json')['versionName']
        args.apk = ROOT / f'artifacts/releases/chick-kitchen-{version}.apk'
    args.apk = args.apk.resolve()
    args.backup_root = args.backup_root.resolve()
    return args


def main():
    try:
        result = Updater(arguments()).perform()
        if result['status'] == 'checked':
            print(json.dumps(result, ensure_ascii=False, indent=2))
        return 0
    except (UpdateError, OSError, ValueError, subprocess.TimeoutExpired) as error:
        print(f'已停止：{error}', file=sys.stderr, flush=True)
        return 1


if __name__ == '__main__':
    sys.exit(main())
