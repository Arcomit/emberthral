# 模组介绍

[![License](https://img.shields.io/badge/License-LGPL--3.0-blue?style=for-the-badge)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green?style=for-the-badge)](https://www.minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-Compatible-orange?style=for-the-badge)](https://neoforged.net/)

**淬火炼刀通用库**，后续作为**尼格洛兹模组**官方版本（暂未发布）的前置模组使用，该库核心功能聚焦于**渲染及概率计算**。

---

## 🌟 提供的特性

### ✨ 创造物品栏分类
- 可以方便快捷的给任意创造物品栏标签页添加类似 [MrCrayfish 的家具：重制](https://www.curseforge.com/minecraft/mc-mods/refurbished-furniture) 模组中的创造物品栏分类按钮
- **注：mc1.20.1-1.0.2起已改为数据包驱动**
- 📖 开发者如何快捷生成数据包见：[例子](#)

### 🎨 高性能模型渲染
- **兼容加速渲染的OBJ模型渲染**（1.21.1+）
- **兼容加速渲染的基岩版模型动画渲染**（仅1.21.1+）

### 🎲 概率计算
- **PRD 随机算法**（Pseudo-Random Distribution）

### 🎯 实体检测工具
- 获取玩家视线所瞄准的实体（可设置辅瞄角度、极限瞄准距离）
- 获取离玩家最近的实体（可设置最大距离）

### ⚔️ 附魔系统扩展
- 通过配置文件自定义附魔兼容/附魔冲突

---

## 🚀 未来计划

- [ ] **后处理渲染框架**
- [ ] 移除获取玩家视线所瞄准的实体等功能，本前置库主要功能转向**渲染和概率计算**
- [ ] 移除自定义附魔兼容/附魔冲突，将该功能独立

---

## 📁 项目结构

```
src/main/java/mod/arcomit/emberthral/
├── content/    # 实际模组内容
├── core/       # 底层核心代码
│   ├── bedrock/         # 基岩版模型动画读取加载渲染
│   └── obj/             # obj模型读取加载渲染
└── util/       # 通用工具库
```

---

## 📚 本项目参考使用以下开源组件的部分代码与实现思路

### 1. [SlashBlade_Resharped](https://github.com/0999312/SlashBlade_Resharped)（OBJ模型渲染）
- **作者**: Furia, 0999312
- **许可证**: MIT License

### 2. [SimpleBedrockModel](https://github.com/MCModderAnchor/SimpleBedrockModel)（基岩版模型动画渲染）
- **作者**: MCModderAnchor
- **许可证**: LGPL-3.0 License

### 3. [EpicACG](https://github.com/dfdyz/EpicACG-1.20)（后处理框架）
- **作者**: dfdyz
- **许可证**: LGPL-2.1 License

### 4. [MaydayAnimationEngine](https://github.com/286799714/MaydayAnimationEngine)
- **作者**: 286799714
- **许可证**: MIT License

---

## 📄 许可证

本项目采用 [LGPL-3.0 License](LICENSE) 开源协议。

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

## 📧 联系方式

如有问题或建议，请通过 Issue 联系我们。

