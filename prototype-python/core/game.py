import pygame
import sys
from settings import *
from core.resource_manager import ResourceManager
from scenes.menu_scene import MenuScene
from scenes.kitchen_scene import KitchenScene  # <--- 1. 导入

class Game:
    def __init__(self):
        pygame.init()
        self.screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT))
        pygame.display.set_caption(SCREEN_TITLE)
        self.clock = pygame.time.Clock()
        
        self.res_manager = ResourceManager()
        
        self.scenes = {
            "menu": MenuScene(self),
            "kitchen": KitchenScene(self),     # <--- 2. 注册
        }
        self.current_scene = self.scenes["menu"]

    # ... (change_scene 和 run 方法保持不变)
    def change_scene(self, scene_name):
        if scene_name in self.scenes:
            self.current_scene = self.scenes[scene_name]
        else:
            print(f"❌ 场景不存在: {scene_name}")

    def run(self):
        while True:
            events = pygame.event.get()
            for event in events:
                if event.type == pygame.QUIT:
                    pygame.quit()
                    sys.exit()

            self.current_scene.handle_events(events)
            self.current_scene.update()
            
            self.screen.fill(BLACK)
            self.current_scene.draw(self.screen)
            
            pygame.display.flip()
            self.clock.tick(FPS)