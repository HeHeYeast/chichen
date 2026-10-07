# bottom-nav-compact-v1

Farm UI V1 精修版的独立底栏方案。`index.html` 是可复用视觉源，默认农场选中；可用 `?active=kitchen|farm|business|explore|book` 查看任一页面的选中态。`bottom-nav-compact-v1-390x79.png` 是完整底栏图，`bottom-nav-compact-v1-states-390x395.png` 展示同一方案的五个选中状态。

## 尺寸与状态

| 项目 | 规则 |
| --- | --- |
| 设计宽度 / 底栏高度 | 390 × 79 px；其他宽度按容器等分 |
| Tab 顺序 | 厨房 / 农场 / 生意 / 寻访 / 图鉴 |
| 等分 | CSS `grid-template-columns: repeat(5, 1fr)`；390 px 时每栏 78 px，中心点 39 / 117 / 195 / 273 / 351 px |
| 图标 | 未选中 36 × 36 px；选中 38 × 38 px；原始矢量 `viewBox` 为 40 × 40；统一 2.2 px 深棕描边 |
| 文字 | 未选中 11 px，选中 12 px；行高 13 px；圆润重字重 |
| 图文间距 | 2 px；图文组合在可用高度内水平、垂直居中 |
| 选中底座 | 每栏左右各 5 px，390 px 屏上为 68 × 67 px；上距内容区 4 px、下距 5 px；17 px 圆角、2 px 浅棕描边 |
| Padding / 安全边距 | 底栏无额外水平 padding，五栏均分；顶部 3 px 描边；图文无固定上下 padding，由 `justify-content: center` 居中。首末栏中心距屏边各 39 px |
| 色彩 | 奶油白底 `#fff9e9 → #fff2d5 → #eed8ad`；未选中 `#80583d`；选中 `#633721`；浅金底座 `#ffe3a0 → #fbd690` |
| 状态 | 只有当前页面带 `.selected` 和 `aria-current="page"`；其余四栏为未选中态 |

制作 Runtime Asset 时保留此尺寸、描边和图标比例；使用 5 枚正式矢量图标、底栏背景与选中底座。此目录不修改任何 Runtime 文件。
