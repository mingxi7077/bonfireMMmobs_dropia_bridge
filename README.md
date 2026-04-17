# bonfireMMmobs_dropia_bridge

[English](#english) | [简体中文](#简体中文)

bonfireMMmobs_dropia_bridge is a Bonfire loot bridge between MythicMobs and ItemsAdder.

bonfireMMmobs_dropia_bridge 是一个连接 MythicMobs 与 ItemsAdder 的 Bonfire 掉落桥接插件。

---

## English

`bonfireMMmobs_dropia_bridge` turns MythicMobs kill events into ItemsAdder-backed ground loot for the Bonfire custom drop pipeline.

### What It Does

- Hooks MythicMobs death events and resolves Bonfire loot rules.
- Spawns ItemsAdder-backed drops through a dedicated bridge layer.
- Keeps drop logic decoupled from mob definition files.
- Provides runtime control through the `/mmdropia` command.

### Core Command

- `/mmdropia <reload|spawn>`

### Repository Layout

- `src/`: plugin source code
- `pom.xml`: Maven build definition
- `target/`: local build output, excluded from release tracking

### Build

```powershell
.\mvnw.cmd -q -DskipTests package
```

### License

This repository currently uses the `Bonfire Non-Commercial Source License 1.0`.
See [LICENSE](LICENSE) for the exact terms.

---

## 简体中文

`bonfireMMmobs_dropia_bridge` 用于把 MythicMobs 的击杀事件转成基于 ItemsAdder 的地面掉落，是 Bonfire 自定义掉落流程中的桥接层。

### 它的作用

- 监听 MythicMobs 死亡事件并解析 Bonfire 的掉落规则。
- 通过专门的桥接逻辑生成 ItemsAdder 支持的掉落物。
- 让掉落逻辑与怪物定义文件解耦，便于独立维护。
- 通过 `/mmdropia` 提供运行时控制。

### 主要命令

- `/mmdropia <reload|spawn>`

### 仓库结构

- `src/`：插件源码
- `pom.xml`：Maven 构建定义
- `target/`：本地构建输出，不纳入发布源码

### 构建方式

```powershell
.\mvnw.cmd -q -DskipTests package
```

### 授权

本仓库当前采用 `Bonfire Non-Commercial Source License 1.0`。
具体条款见 [LICENSE](LICENSE)。
