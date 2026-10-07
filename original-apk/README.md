# 原版安装包解包残留

原版 APK 解包后运行时用不到的部分，只作溯源保留：`classes.dex`（可用 `dexdump -d` 核对反编译不完整的方法）、`AndroidManifest.xml`、`resources.arsc`、签名目录 `META-INF/`、`lib/`、`org/` 与广告 SDK 图片 `vpon_*.png`。

同一个安装包里被游戏直接使用的 `assets/`（原始数据 XML、图片、音效）和 `res/` 仍留在项目根目录，`server.mjs`、Web 代码和 Android 打包都按根目录路径读取它们。反编译 Java 源码在 `artifacts/original-source/`。
