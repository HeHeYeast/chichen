import os

# --- 屏幕与帧率 ---
SCREEN_WIDTH = 540
SCREEN_HEIGHT = 960
SCREEN_TITLE = "鸡宝厨房重制版"
FPS = 30

# --- 路径配置 ---
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
ASSETS_DIR = os.path.join(BASE_DIR, "assets", "png")
DATA_DIR = os.path.join(BASE_DIR, "data")

# --- 颜色定义 (R, G, B) ---
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
RED = (255, 50, 50)
GREEN = (50, 200, 50)
BLUE = (50, 50, 255)      # <--- 补上了这个
ORANGE = (255, 165, 0)
BTN_COLOR = (255, 200, 100)
BG_COLOR_DEFAULT = (240, 230, 200)
YELLOW = (255, 215, 0)      # 星星/金币
BROWN_DARK = (101, 67, 33)  # UI边框
BROWN_LIGHT = (222, 184, 135)# UI背景
BAR_BG = (50, 30, 10)       # 进度条底色
BAR_FILL = (255, 140, 0)    # 进度条填充色

# --- 字体配置 ---
FONT_FONTS = ["microsoftyahei", "simhei", "simsun", "arialunicode"]