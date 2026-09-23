# Chicken UI：游戏内中文字体

游戏使用 Google Fonts 官方仓库中的 **Noto Sans SC** 可变字体作为中文基础字体，在 CSS 中使用 `Chicken UI` 这个别名。原始字体的版权、作者、许可和名称记录全部保留；子集没有转换成某个固定字重。

## 官方来源与许可

- [Google Fonts / Noto Sans SC 目录](https://github.com/google/fonts/tree/main/ofl/notosanssc)
- [官方原始可变 TTF](https://raw.githubusercontent.com/google/fonts/main/ofl/notosanssc/NotoSansSC%5Bwght%5D.ttf)
- [官方 SIL Open Font License 1.1](https://raw.githubusercontent.com/google/fonts/main/ofl/notosanssc/OFL.txt)
- 随游戏分发的完整许可：`OFL-NotoSansSC.txt`，从上面的官方地址原样下载。

获取日期：2026-09-08。未修改的原始字体仅作为构建输入保存在 `artifacts/font-source/NotoSansSC-wght.ttf`，大小为 17,772,300 字节，不由游戏页面加载。

原始文件 SHA-256：

```text
a3041811a78c361b1de50f953c805e0244951c21c5bd412f7232ef0d899af0da
```

## 当前产物

- `chick-ui.woff2`：651,676 字节，约 636 KiB（2026-09-23 Work K 重建，覆盖扩展内容与五入口新文案）。
- `font.css`：定义 `font-family: 'Chicken UI'`、`font-weight: 100 900`。
- `coverage.json`：本次构建的输入文件、覆盖字符数、字重轴与命名记录验证结果。

本次扫描 `web` 根目录下全部 `.js` / `.html` 文件与 Android Java 文本，当前可显示字符 2,044 个，加上 ASCII 与常用标点后共 2,084 个 Unicode 码位，**缺字数为 0**（含地区/常客/项目/收藏生成文本）。`wght` 轴完整保留为 100–900；原始名称表记录逐条一致。

扫描包含现有角色名、厨具名、调味料名、提示与确认文案，也包含根目录审阅页面中的文字；不递归扫描任何目录，因此不会把 `web/baseline-*` 中的旧版本加入子集。审阅工具页 `review.html` 和 `compare.html` 本身的字体没有修改。

## 重新生成与检查

从项目根目录执行，使用 Python、FontTools 4.62.1 和 Brotli：

```powershell
python tools/build-font.py
python tools/build-font.py --check
```

第一条命令重新扫描当前文本，生成 WOFF2、对应 CSS 与覆盖报告；第二条只检查当前文字能否被已有产物覆盖，不修改文件。新增角色名或文案以后重新执行这两步。

原始字体不存在时，脚本会报告官方原文件下载地址。请将未修改的文件放回 `artifacts/font-source/NotoSansSC-wght.ttf`，并用上述 SHA-256 核对本次锁定的构建输入。也可用 `--source <path>` 指定本地输入。

`--format auto` 为默认值，Brotli 可用时生成 WOFF2；Brotli 不可用时生成 `chick-ui.ttf` 并同步更新 `font.css`。需要明确选择格式时：

```powershell
python tools/build-font.py --format woff2
python tools/build-font.py --format ttf
```

构建保留所有名称与语言记录、布局特性、可变字重轴和命名实例。脚本校验源字体字符覆盖、产物字符覆盖、原始名称记录与字重范围，不通过时退出失败。字体重新打包时不会重设源字体的时间戳。

## 接入位置

- 游戏 DOM：`web/style.css` 首行导入 `font.css`，`--font` 将 `Chicken UI` 放在首位。
- 游戏画布：`web/theme.js` 的 `FONT` 使用同一个精确族名。
- 规范板：`web/art-board.html` 加载同一份字体，正文和游戏文字样例使用它。
- 系统字体保留为回退链，用于字体请求失败或未来尚未加入子集的文字。

字体文件成功加载与实际中文字形清晰度仍需通过浏览器检查。字符覆盖检查保证有对应字形，不代替字号、排版或美术验收。
