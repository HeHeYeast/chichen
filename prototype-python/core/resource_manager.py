import pygame
import os
from settings import ASSETS_DIR, FONT_FONTS

class ResourceManager:
    def __init__(self):
        self.images = {}
        self.fonts = {}
        
    def load_image(self, relative_path, key_name=None):
        """
        加载图片并缓存。
        relative_path: 例如 'MainMenu/main_bg.jpg'
        """
        if key_name is None:
            key_name = relative_path

        if key_name in self.images:
            return self.images[key_name]

        full_path = os.path.join(ASSETS_DIR, relative_path)
        
        # 自动纠错：尝试 .jpg 和 .png
        if not os.path.exists(full_path):
            base, ext = os.path.splitext(full_path)
            if ext == '.jpg': full_path = base + '.png'
            elif ext == '.png': full_path = base + '.jpg'

        if os.path.exists(full_path):
            try:
                image = pygame.image.load(full_path).convert_alpha()
                self.images[key_name] = image
                return image
            except Exception as e:
                print(f"❌ 图片损坏 {full_path}: {e}")
        else:
            print(f"⚠️ 资源缺失: {relative_path}")
        
        return None

    def get_font(self, size=24):
        key = f"font_{size}"
        if key in self.fonts:
            return self.fonts[key]
        
        font = pygame.font.Font(None, size) # 默认
        for name in FONT_FONTS:
            try:
                font = pygame.font.SysFont(name, size)
                break
            except:
                continue
        self.fonts[key] = font
        return font