import json
import os
from settings import DATA_DIR

class DataManager:
    def __init__(self):
        self.cookware = {}
        self.eggs = {}
        self.chickens = {}
        
        # 加载数据
        self.load_all()

    def load_json(self, filename):
        path = os.path.join(DATA_DIR, filename)
        if not os.path.exists(path):
            print(f"⚠️ 警告: 数据文件缺失 {filename}")
            return None
        try:
            with open(path, 'r', encoding='utf-8') as f:
                return json.load(f)
        except Exception as e:
            print(f"❌ 读取 {filename} 失败: {e}")
            return None

    def load_all(self):
        # 1. 加载厨具
        data = self.load_json("cookware.json")
        if data:
            for tool in data.get("tools", []):
                self.cookware[tool["id"]] = tool
        
        # 2. 加载蛋
        data = self.load_json("eggs.json")
        if data:
            for egg in data.get("tools", []):
                self.eggs[egg["id"]] = egg

        # 3. 加载鸡 (简单加载，用于后续查询)
        data = self.load_json("characters_chicken.json")
        if data:
            for char in data.get("characters", []):
                self.chickens[char["id"]] = char

    def get_tool_info(self, tool_id):
        # 如果没加载到文件，返回默认测试数据
        default = {
            "name": {"zh_CN": "未知厨具"},
            "levels": [{"cook_time": 5}] # 默认5秒
        }
        return self.cookware.get(tool_id, default)