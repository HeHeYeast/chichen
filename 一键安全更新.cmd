@echo off
chcp 65001 >nul
echo 鸡宝厨房安全更新：请连接手机并允许 USB 调试。
echo 更新期间请暂停操作游戏；备份失败会停止安装。
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0tools\install-android.ps1" -Launch %*
if errorlevel 1 (
  echo 更新未完成，请查看上方原因。已有备份会保留。
) else (
  echo 更新完成。
)
pause
