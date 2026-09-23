# 原生孵化提醒的行为检查

运行 `android/tests/run-notification-tests.ps1`，可用 `-JavaHome` 指定 JDK 17。脚本直接编译并执行生产的 `HatchScheduler`、`HatchAlarmReceiver`、`RestoreAlarmsReceiver`，输出到 `android/build/notification-test-classes`。

这里的 Android 服务、Intent、SharedPreferences 是 JVM 测试替身；JSON 使用 `../aosp-json` 中 Android 官方开源实现。检查的是生产调度类的分支、状态和调用效果，不模拟操作系统的后台限制。**通过这些检查不代表已在 Magic8 真机验证。**

当前覆盖 47 条行为断言：

- 整批结束和剩余鸡宝的最晚孵化时间取最大值，再加 3 秒；重复保存不会反复排队。
- 唯一不可变 PendingIntent；换批、全收取、关闭提醒时取消旧日程。
- 已过期批次补发一次；前台 `batch.alarmed` 不影响原生通知；关闭再开启或恢复近期存档不重复提醒。
- Android 13 通知权限、应用通知开关、频道开关、精确提醒权限和授权后恢复。
- 精确提醒被拒时使用非精确提醒，并在状态中说明可能延迟。
- 重启、覆盖更新、系统时间/时区、精确权限恢复广播；拒绝无关 action。
- 测试通知不改变实际日程或已送达标记。
- 通知/调度/持久化失败后恢复；无效超大时间戳；Android 8 权限分支。

真机验收仍需确认：允许/拒绝通知后的设置反馈、退到后台与锁屏到期、关机重启后的恢复、覆盖安装后的恢复、点击通知进入厨房、MagicOS 省电策略下的实际延迟。应用被用户强行停止时，Android 不保证后台闹钟继续运行；重新启动应用会从存档恢复。

## 官方依据

- [Android 闹钟调度](https://developer.android.com/develop/background-work/services/alarms)：PendingIntent 允许应用不运行时接收提醒；相同 PendingIntent 替换原有闹钟；非精确提醒可能受省电策略延迟。
- [AlarmManager API](https://developer.android.com/reference/android/app/AlarmManager)：Android 12+ 调用精确提醒前检查 `canScheduleExactAlarms()`；精确提醒权限撤销会删除对应闹钟，授权广播只在授予时发送。因此应用回到前台也要重新检查。
- [Android 通知权限](https://developer.android.com/develop/ui/compose/notifications/notification-permission)：Android 13+ 普通通知需 `POST_NOTIFICATIONS` 运行时授权。
- [通知频道](https://developer.android.com/develop/ui/compose/notifications/channels)：Android 8+ 使用通知频道；用户可关闭频道，因此只检查运行时权限不够。

不申请 `USE_EXACT_ALARM`，不使用后台保活服务，也不自动申请精确提醒的特殊权限。用户可自行打开系统精确提醒设置；未授予时仍提供普通孵化提醒。
