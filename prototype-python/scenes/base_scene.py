class BaseScene:
    def __init__(self, game):
        self.game = game  # 持有主游戏对象的引用
        
    def handle_events(self, events):
        """处理输入事件"""
        pass
        
    def update(self):
        """处理逻辑更新"""
        pass
        
    def draw(self, screen):
        """绘制画面"""
        pass