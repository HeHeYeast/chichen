import pygame
import time
import math
import random
from settings import *

class Slot:
    def __init__(self, x, y, width, height):
        self.rect = pygame.Rect(x, y, width, height)
        
        # 状态机: EMPTY -> SELECTED -> COOKING -> READY
        # 注意：READY 状态下，具体哪只鸡还在，由 self.eggs 里的状态决定
        self.state = "EMPTY"
        
        self.egg_type = 0
        self.tool_id = 0      
        self.start_time = 0
        self.total_time = 5   
        self.result_id = 0
        
        # 存储每一颗蛋的数据：{'rect': Rect, 'offset': (x,y), 'active': True}
        self.eggs = []
        self.init_egg_positions()

    def init_egg_positions(self):
        """预计算蛋的堆叠排布坐标"""
        self.eggs = []
        rows = 4
        cols = 6

        # 蛋的点击区域尺寸
        egg_w = 70
        egg_h = 80

        # 间距设置 - 配合更大的蛋
        gap_x = 52
        gap_y = 45

        # 整体居中计算
        total_w = (cols - 1) * gap_x
        start_x = self.rect.centerx - total_w // 2
        start_y = self.rect.y + 25

        for r in range(rows):
            for c in range(cols):
                base_x = start_x + c * gap_x
                base_y = start_y + r * gap_y

                # 轻微随机偏移
                dx = random.randint(-3, 3)
                dy = random.randint(-3, 3)

                # 图层：下方行遮挡上方行，同一行内随机遮挡
                # 行号 * 100 保证下方行一定在上方行之上，行内随机0-99
                z = r * 100 + random.randint(0, 99)

                egg_rect = pygame.Rect(base_x + dx - egg_w//2, base_y + dy - egg_h//2, egg_w, egg_h)

                self.eggs.append({
                    "rect": egg_rect,
                    "draw_pos": (base_x + dx, base_y + dy),
                    "active": True,
                    "z": z
                })

        # 按z值排序，z小的先画（被遮挡），z大的后画（遮挡别人）
        self.eggs.sort(key=lambda e: e["z"])

    def select_egg(self, egg_id):
        if self.state == "EMPTY" or self.state == "SELECTED":
            self.state = "SELECTED"
            self.egg_type = egg_id
            # 重置所有蛋为激活状态
            for egg in self.eggs:
                egg["active"] = True
            return True
        return False

    def start_cooking(self, tool_id, duration):
        self.state = "COOKING"
        self.tool_id = tool_id
        self.total_time = duration
        self.start_time = time.time()

    def handle_input(self, mouse_pos, mouse_pressed):
        """处理鼠标交互 (点击或滑动)"""
        harvest_count = 0
        
        if self.state == "READY":
            # 倒序遍历 (从前排往后排检测)，符合视觉直觉
            for egg in reversed(self.eggs):
                if egg["active"]:
                    if egg["rect"].collidepoint(mouse_pos):
                        # 如果是点击或者按住滑动
                        if mouse_pressed:
                            egg["active"] = False
                            harvest_count += 1
                            # 播放音效? 
                            
            # 检查是否全部收完
            active_count = sum(1 for e in self.eggs if e["active"])
            if active_count == 0:
                self.state = "EMPTY"
                print("✨ 这一窝收完了")
        
        return harvest_count

    def update(self):
        if self.state == "COOKING":
            if time.time() - self.start_time >= self.total_time:
                self.state = "READY"
                self.result_id = 0 

    def draw(self, screen, res_manager, font):
        # 调试：画出鸟巢判定范围
        # pygame.draw.rect(screen, RED, self.rect, 1)

        if self.state in ["SELECTED", "COOKING", "READY"]:
            
            # 确定图片
            if self.state == "READY":
                img_key = f"Character/character_0/character_0_0_0_0.png"
                fallback = "chick_0"
            else:
                img_key = f"Egg/egg_{self.egg_type}_0_0.png"
                fallback = "egg_normal"
            
            orig_img = res_manager.load_image(img_key, fallback)
            
            if orig_img:
                # 统一缩放 - 蛋更大一些，纵向拉长
                target_w = 95 if self.state != "READY" else 100
                ratio = target_w / orig_img.get_width()
                target_h = int(orig_img.get_height() * ratio * 1.15)  # 纵向增加15%
                scaled_img = pygame.transform.scale(orig_img, (target_w, target_h))
                
                # 遍历绘制所有 ACTIVE 的蛋
                for egg in self.eggs:
                    if not egg["active"]:
                        continue
                        
                    # 呼吸动画
                    anim_y = 0
                    if self.state == "COOKING":
                        # 基于位置的波浪
                        anim_y = int(math.sin(time.time()*5 + egg["draw_pos"][0]/50) * 2)
                    
                    # 居中绘制图片
                    draw_x = egg["draw_pos"][0] - target_w // 2
                    draw_y = egg["draw_pos"][1] - target_h // 2 + anim_y
                    
                    screen.blit(scaled_img, (draw_x, draw_y))
                    
                    # 调试：画出每个蛋的点击区域
                    # pygame.draw.rect(screen, (0,255,0), egg["rect"], 1)

        # 进度条
        if self.state == "COOKING":
            elapsed = time.time() - self.start_time
            progress = min(elapsed / self.total_time, 1.0)
            
            bar_w = 240
            bar_h = 24
            bar_x = self.rect.centerx - bar_w // 2
            bar_y = self.rect.bottom + 10 # 下移
            
            # 样式优化：深色底 + 亮色条
            pygame.draw.rect(screen, (40, 20, 10), (bar_x, bar_y, bar_w, bar_h), border_radius=12)
            pygame.draw.rect(screen, (200, 180, 150), (bar_x, bar_y, bar_w, bar_h), 2, border_radius=12)
            
            fill_w = int((bar_w-6) * progress)
            if fill_w > 0:
                pygame.draw.rect(screen, (255, 140, 0), (bar_x+3, bar_y+3, fill_w, bar_h-6), border_radius=10)
            
            # 闹钟
            alarm_img = res_manager.load_image("MainGame/alarm_on.png")
            if alarm_img:
                alarm_scaled = pygame.transform.scale(alarm_img, (32, 32))
                screen.blit(alarm_scaled, (bar_x - 16, bar_y - 4))