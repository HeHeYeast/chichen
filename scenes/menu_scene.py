import pygame
from scenes.base_scene import BaseScene
from settings import *

class MenuScene(BaseScene):
    def __init__(self, game):
        super().__init__(game)
        self.bg = self.game.res_manager.load_image("MainMenu/main_bg.jpg", "menu_bg")
        self.logo = self.game.res_manager.load_image("MainMenu/main_logo_cn.png", "logo")
        #稍微调大一点字体
        self.font = self.game.res_manager.get_font(40)
        
        # 按钮居中偏下
        cx = SCREEN_WIDTH // 2
        self.btn_rect = pygame.Rect(cx - 120, SCREEN_HEIGHT - 250, 240, 70)

    def handle_events(self, events):
        for event in events:
            if event.type == pygame.MOUSEBUTTONDOWN:
                if self.btn_rect.collidepoint(event.pos):
                    # 这里切换到 kitchen，必须确保 Game 类里注册了这个 key
                    self.game.change_scene("kitchen")

    def draw(self, screen):
        # 1. 背景
        if self.bg:
            screen.blit(self.bg, (0, 0))
        else:
            screen.fill(ORANGE)

        # 2. Logo
        if self.logo:
            lx = (SCREEN_WIDTH - self.logo.get_width()) // 2
            screen.blit(self.logo, (lx, 100))

        # 3. 按钮 (圆角矩形 + 边框 + 文字居中)
        pygame.draw.rect(screen, BTN_COLOR, self.btn_rect, border_radius=15)
        pygame.draw.rect(screen, BLACK, self.btn_rect, 4, border_radius=15)
        
        # 文字渲染与居中
        text_surf = self.font.render("开始游戏", True, BLACK)
        # 获取文字的矩形区域，并将其中心点设置为按钮的中心点
        text_rect = text_surf.get_rect(center=self.btn_rect.center)
        screen.blit(text_surf, text_rect)