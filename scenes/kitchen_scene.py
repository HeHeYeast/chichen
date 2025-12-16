import pygame
from scenes.base_scene import BaseScene
from entities.slot import Slot
from core.data_manager import DataManager
from settings import *

class KitchenScene(BaseScene):
    def __init__(self, game):
        super().__init__(game)
        
        self.data_manager = DataManager()
        self.bg = self.game.res_manager.load_image("Tool/Tool0/tool_0_0_0_0.jpg", "kitchen_lv0")
        
        # 字体
        try:
            self.font_ui = pygame.font.SysFont("simhei", 20, bold=True)
            # 顶栏字体稍微大一点
            self.font_top = pygame.font.SysFont("simhei", 26, bold=True)
            self.font_big = pygame.font.SysFont("simhei", 28, bold=True)
        except:
            self.font_ui = self.game.res_manager.get_font(20)
            self.font_top = self.game.res_manager.get_font(26)
            self.font_big = self.game.res_manager.get_font(28)
        
        # 1. 坐标修正
        # 孵化区：根据反馈，调整位置。
        # 之前 y=330，现在往下稍微移一点点或者保持，重点是高度要能容纳大蛋
        nest_w, nest_h = 340, 260 
        nest_x = (SCREEN_WIDTH - nest_w) // 2
        nest_y = 320 # 稍微上提一点点留出下方空间
        self.slot = Slot(nest_x, nest_y, nest_w, nest_h)
        
        self.spice_rect = pygame.Rect(30, 140, 130, 100)
        self.selected_spice_id = None

        # 调换调味料按钮（放在调味区左侧）
        self.btn_change_spice = pygame.Rect(5, 170, 40, 40)

        # 蛋按钮
        self.btn_egg_chicken = pygame.Rect(35, 245, 60, 60)
        self.btn_egg_duck = pygame.Rect(105, 245, 60, 60)
        
        # 2. 顶部UI
        self.top_bar_height = 60
        self.buttons_top = ["厨房", "农场", "商店", "其他"]
        self.btn_rects = []
        btn_w = SCREEN_WIDTH // 4
        for i in range(4):
            self.btn_rects.append(pygame.Rect(i * btn_w, 0, btn_w, self.top_bar_height))

        # 3. 数据
        self.money = 728
        self.level = 11
        
        self.show_toolbar = False
        self.tools = []
        self.load_tools_from_data()
        self.back_btn = pygame.Rect(10, 10, 80, 40)
        
        # 鼠标状态记录 (用于滑动收获)
        self.is_dragging = False

    def load_tools_from_data(self):
        # ... (保持不变)
        target_ids = [0, 1, 2, 3]
        margin_left = 20
        item_w = 100
        gap = 10
        for i, tid in enumerate(target_ids):
            info = self.data_manager.get_tool_info(tid)
            img_path = f"Tool/Tool1/tool_1_{tid}_0_0.png"
            img = self.game.res_manager.load_image(img_path, f"tool_icon_{tid}")
            
            x = margin_left + i * (item_w + gap)
            y = SCREEN_HEIGHT - 130
            rect = pygame.Rect(x, y, item_w, 120)
            self.tools.append({"id": tid, "info": info, "image": img, "rect": rect})

    def handle_events(self, events):
        # 获取每一帧的鼠标状态，用于“按住滑动”
        mouse_pos = pygame.mouse.get_pos()
        mouse_pressed = pygame.mouse.get_pressed()[0] # 左键是否按住

        # --- 实时处理滑动收获 ---
        if self.slot.state == "READY":
            # 传递给 Slot 处理具体的碰撞逻辑
            harvested = self.slot.handle_input(mouse_pos, mouse_pressed)
            if harvested > 0:
                self.money += harvested * 1 # 每只1块钱
                # 这里可以播放 "啵" 的音效

        for event in events:
            if event.type == pygame.MOUSEBUTTONDOWN:
                # 选蛋
                if self.slot.state == "EMPTY":
                    if self.btn_egg_chicken.collidepoint(event.pos):
                        self.slot.select_egg(0) 
                        return
                    if self.btn_egg_duck.collidepoint(event.pos):
                        self.slot.select_egg(1) 
                        return

                # 厨具栏
                if self.show_toolbar:
                    for tool in self.tools:
                        if tool["rect"].collidepoint(event.pos):
                            levels = tool["info"].get("levels", [])
                            cook_time = levels[0]["cook_time"] if levels else 5
                            self.slot.start_cooking(tool["id"], cook_time)
                            self.show_toolbar = False
                            return
                
                # 鸟巢点击 (仅处理弹出菜单，收获逻辑已移至上方滑动处理)
                if self.slot.rect.collidepoint(event.pos):
                    if self.slot.state == "SELECTED":
                        self.show_toolbar = not self.show_toolbar

    def update(self):
        self.slot.update()

    def draw(self, screen):
        # 1. 背景
        if self.bg:
            bg_w = self.bg.get_width()
            bg_h = self.bg.get_height()
            scale_ratio = SCREEN_HEIGHT / bg_h 
            new_w = int(bg_w * scale_ratio)
            new_h = SCREEN_HEIGHT
            scaled_bg = pygame.transform.scale(self.bg, (new_w, new_h))
            draw_x = (SCREEN_WIDTH - new_w) // 2
            screen.blit(scaled_bg, (draw_x, 0))
        else:
            screen.fill(WHITE)
            
        # 2. 蛋按钮 (只画图)
        self.draw_egg_button(screen, self.btn_egg_chicken, 0)
        self.draw_egg_button(screen, self.btn_egg_duck, 1)

        # 2.5 调换调味料按钮
        change_icon = self.game.res_manager.load_image("MainGame/change_icon.png")
        if change_icon:
            icon_scaled = pygame.transform.scale(change_icon, (36, 36))
            icon_rect = icon_scaled.get_rect(center=self.btn_change_spice.center)
            screen.blit(icon_scaled, icon_rect)

        # 3. 槽位
        self.slot.draw(screen, self.game.res_manager, self.font_ui)
        
        # 4. 顶部 UI (完全翻新)
        self.draw_top_bar(screen)

        # 5. 右上角星星
        star_img = self.game.res_manager.load_image("MainGame/level_star.png")
        if star_img:
            star_x = SCREEN_WIDTH - 85
            star_y = 70
            screen.blit(star_img, (star_x, star_y))
            # 这里的数字逻辑保持不变，你根据图片是否自带数字决定是否开启绘制
            # level_str = str(self.level)
            # ... (同上个版本，略)

        # 6. 右下角金币（带透明度的圆角矩形）
        cp_w, cp_h = 120, 40
        cp_x = SCREEN_WIDTH - cp_w - 20
        cp_y = SCREEN_HEIGHT - 60

        # 创建带透明度的背景
        cp_surface = pygame.Surface((cp_w, cp_h), pygame.SRCALPHA)
        pygame.draw.rect(cp_surface, (60, 40, 20, 180), (0, 0, cp_w, cp_h), border_radius=20)
        pygame.draw.rect(cp_surface, (180, 140, 80, 200), (0, 0, cp_w, cp_h), 2, border_radius=20)
        screen.blit(cp_surface, (cp_x, cp_y))

        # 金币图标
        coin_img = self.game.res_manager.load_image("MainGame/coin.png")
        if coin_img:
            c_scaled = pygame.transform.scale(coin_img, (32, 32))
            screen.blit(c_scaled, (cp_x + 5, cp_y + 4))

        # 金币数字
        money_txt = self.font_ui.render(f"{self.money}cp", True, (255, 220, 100))
        money_rect = money_txt.get_rect(midleft=(cp_x + 40, cp_y + cp_h // 2))
        screen.blit(money_txt, money_rect)

        # 7. 厨具栏
        if self.show_toolbar:
            self.draw_toolbar(screen)

    def draw_top_bar(self, screen):
        """重绘顶栏"""
        # 边框颜色 (深褐)
        border_col = (80, 50, 30)
        # 文字颜色 (深褐)
        text_col = (90, 60, 40)
        
        for i, rect in enumerate(self.btn_rects):
            is_active = (i == 0) # 厨房激活
            
            # 背景色：激活=橙色，未激活=米白
            bg_color = (255, 170, 0) if is_active else (253, 250, 240)
            
            pygame.draw.rect(screen, bg_color, rect)
            
            # 画边框 (上、左、右，底部如果是激活态则不画，制造连通感)
            # 粗一点的线条
            line_w = 4
            pygame.draw.line(screen, border_col, rect.topleft, rect.topright, line_w) # 上
            pygame.draw.line(screen, border_col, rect.topleft, rect.bottomleft, line_w) # 左
            
            # 最后一个按钮画右边框
            if i == len(self.btn_rects) - 1:
                pygame.draw.line(screen, border_col, rect.topright, rect.bottomright, line_w)
            
            # 底部边框：如果是未激活的，要画底边；如果是激活的，不画底边（或画颜色一样的线）
            if not is_active:
                pygame.draw.line(screen, border_col, rect.bottomleft, rect.bottomright, line_w)
            
            # 文字
            txt = self.font_top.render(self.buttons_top[i], True, text_col)
            tr = txt.get_rect(center=rect.center)
            screen.blit(txt, tr)

    def draw_egg_button(self, screen, rect, egg_id):
        # 使用3号蛋作为选择按钮图片
        img_key = f"Egg/egg_{egg_id}_0_3.png"
        icon = self.game.res_manager.load_image(img_key)
        
        if icon:
            scaled = pygame.transform.scale(icon, (55, 65))
            rect_img = scaled.get_rect(center=rect.center)
            screen.blit(scaled, rect_img)

    def draw_toolbar(self, screen):
        # 略，保持之前样式
        bar_h = 160
        bar_bg = pygame.Surface((SCREEN_WIDTH, bar_h))
        bar_bg.set_alpha(240)
        bar_bg.fill((50, 30, 10)) 
        screen.blit(bar_bg, (0, SCREEN_HEIGHT - bar_h))
        
        for tool in self.tools:
            pygame.draw.rect(screen, (255, 240, 200), tool["rect"], border_radius=10)
            pygame.draw.rect(screen, (101, 67, 33), tool["rect"], 3, border_radius=10)
            
            if tool["image"]:
                icon = pygame.transform.scale(tool["image"], (70, 70))
                icon_rect = icon.get_rect(center=(tool["rect"].centerx, tool["rect"].centery - 15))
                screen.blit(icon, icon_rect)
            
            levels = tool["info"].get("levels", [])
            cost = levels[0]["cook_cost"] if levels else 0
            
            # 价格
            info_txt = self.font_ui.render(f"{cost}cp", True, BLACK)
            screen.blit(info_txt, (tool["rect"].x + 8, tool["rect"].y + 5))