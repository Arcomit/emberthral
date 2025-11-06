# 项目介绍

[![License](https://img.shields.io/badge/License-LGPL--3.0-blue?style=for-the-badge)](LICENSE)

> 一个简易的前置库模组，提供了OBJ模型渲染，基岩版模型动画渲染，后处理框架，创造物品栏分类。

> 本项目参考使用以下开源组件的部分代码与实现思路：
> 1. SlashBlade_Resharped（OBJ模型渲染） - 作者：Furia，0999312
>
>   许可证：MIT License
> 
>   源码地址：https://github.com/0999312/SlashBlade_Resharped
> 2. SimpleBedrockModel（基岩版模型动画渲染） - 作者：MCModderAnchor
>
>   许可证：LGPL-3.0 License
>
>   源码地址：https://github.com/MCModderAnchor/SimpleBedrockModel
> 3. EpicACG（后处理框架） - 作者：dfdyz
>
>   许可证：LGPL-2.1 License
>
>   源码地址：https://github.com/dfdyz/EpicACG-1.20
> 4. MaydayAnimationEngine - 作者：286799714
>
>   许可证：MIT License
> 
>   源码地址：https://github.com/286799714/MaydayAnimationEngine

### 项目结构

```
src/main/java/mod/arcomit/emberthral/
├── content/    # 实际模组内容
├── core/       # 底层核心代码
│   ├── bedrock/         # 基岩版模型动画读取加载渲染
│   └── obj/             # obj模型读取加载渲染
└── util/       # 通用工具库
```
