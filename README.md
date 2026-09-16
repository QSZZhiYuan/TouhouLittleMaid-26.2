# Touhou Little Maid — Minecraft 26.2 NeoForge Port

[中文](#中文说明) | [English](#english-description)

---

## 中文说明

> 一个关于女仆的 Minecraft 模组，本仓库是 [TouhouLittleMaid](https://github.com/TartaricAcid/TouhouLittleMaid) 向 Minecraft 26.2 (NeoForge) 的非官方移植版。

### 简介

本模组受 [Little Maid Mob](https://github.com/MMM666/littleMaidMob) 和 [东方Project](https://zh.wikipedia.org/wiki/东方Project) 启发，为 Minecraft 添加了可爱的女仆，她们可以帮助你战斗、种田、做家务等。模组支持基岩版实体格式的自定义模型，你可以轻松切换女仆模型，或创建自己的女仆模型。

### 移植说明

- **目标版本**：Minecraft 26.2.0 / NeoForge 26.2.0.45-beta / Java 25
- **源版本**：Minecraft 1.21.1 / NeoForge
- 本移植版基于官方 [TouhouLittleMaid](https://github.com/TartaricAcid/TouhouLittleMaid) 1.21.1 分支进行适配
- 参考了非官方 Fabric 移植版 [TouhouLittleMaid-Orihime](https://github.com/Sh1roCu/TouhouLittleMaid-Orihime) 的部分思路
- 已完成服务端启动修复、客户端渲染修复、弹幕系统修复、棋盘悬浮字修复等
- 添加了与 AcademyCraft 模组的兼容性（心理免疫）

### 已知问题与修复状态

| 功能 | 状态 |
|------|------|
| 服务端启动 | ✅ 已修复 |
| 客户端渲染 | ✅ 已修复 |
| 弹幕发射与伤害 | ✅ 已修复 |
| 棋盘悬浮字 | ✅ 已修复 |
| 好感度工具 | ⚠️ 待测试 |

### 构建方法

```bash
# Windows
.\gradlew.bat build

# Linux/macOS
./gradlew build
```

构建产物位于 `build/libs/` 目录下。

### 依赖

- Minecraft 26.2.0
- NeoForge 26.2.0.45-beta 或更高版本
- Java 25

### 致谢

- **TartaricAcid** 及所有贡献者：创建了原版 [TouhouLittleMaid](https://github.com/TartaricAcid/TouhouLittleMaid) 模组
- **Sh1roCu**：非官方 Fabric 移植版 [TouhouLittleMaid-Orihime](https://github.com/Sh1roCu/TouhouLittleMaid-Orihime)
- **MMM 和 Verclene**：制作了 [Little Maid Mob](https://github.com/MMM666/littleMaidMob) 模组
- **ZUN**：制作了 [东方Project](https://en.wikipedia.org/wiki/Touhou_Project)
- 声音素材来自 [あみたろの声素材工房](https://www14.big.or.jp/~amiami/happy/)

### 许可证

- 代码：[MIT](LICENSE-MIT)
- 素材：[CC BY-NC-SA 4.0](LICENSE-CC)

---

## English Description

> A Minecraft mod about maids. This repository is an unofficial port of [TouhouLittleMaid](https://github.com/TartaricAcid/TouhouLittleMaid) to Minecraft 26.2 (NeoForge).

### Introduction

This mod is inspired by [Little Maid Mob](https://github.com/MMM666/littleMaidMob) and [Touhou Project](https://en.wikipedia.org/wiki/Touhou_Project). It adds cute maids to Minecraft who can help you with battles, farming, and other tasks. The mod supports custom models in Bedrock Edition Entity format — you can easily switch maid models or create your own.

### Port Details

- **Target version**: Minecraft 26.2.0 / NeoForge 26.2.0.45-beta / Java 25
- **Source version**: Minecraft 1.21.1 / NeoForge
- This port is based on the official [TouhouLittleMaid](https://github.com/TartaricAcid/TouhouLittleMaid) 1.21.1 branch
- Referenced the unofficial Fabric port [TouhouLittleMaid-Orihime](https://github.com/Sh1roCu/TouhouLittleMaid-Orihime) for some ideas
- Completed fixes: server startup, client rendering, danmaku (bullet) system, chess board floating text, etc.
- Added compatibility with AcademyCraft mod (mental immunity)

### Known Issues & Fix Status

| Feature | Status |
|---------|--------|
| Server startup | ✅ Fixed |
| Client rendering | ✅ Fixed |
| Danmaku shooting & damage | ✅ Fixed |
| Chess board floating text | ✅ Fixed |
| Favorability tool | ⚠️ Needs testing |

### Build

```bash
# Windows
.\gradlew.bat build

# Linux/macOS
./gradlew build
```

The build artifact will be in `build/libs/`.

### Dependencies

- Minecraft 26.2.0
- NeoForge 26.2.0.45-beta or higher
- Java 25

### Credits

- **TartaricAcid** and all contributors: Created the original [TouhouLittleMaid](https://github.com/TartaricAcid/TouhouLittleMaid) mod
- **Sh1roCu**: Unofficial Fabric port [TouhouLittleMaid-Orihime](https://github.com/Sh1roCu/TouhouLittleMaid-Orihime)
- **MMM and Verclene**: Made the [Little Maid Mob](https://github.com/MMM666/littleMaidMob) mod
- **ZUN**: Made the [Touhou Project](https://en.wikipedia.org/wiki/Touhou_Project)
- Sound resources from [あみたろの声素材工房](https://www14.big.or.jp/~amiami/happy/)

### License

- Code: [MIT](LICENSE-MIT)
- Assets: [CC BY-NC-SA 4.0](LICENSE-CC)