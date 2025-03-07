# 🌐 StarBanWorld - 世界权限管理插件 🚫

![License](https://img.shields.io/badge/License-MIT-green.svg)
![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-blueviolet)

## ✨ 功能特性

- 🔒 白名单世界管理系统
- 🛡️ 智能传送事件拦截（末地门/下界传送门/指令传送）
- 📝 可定制的无权限提示消息
- 📋 管理员命令面板
- 🔄 实时配置重载功能
- 👥 完善的权限节点系统

## 📥 安装指南

1. 从Releases下载最新版本插件jar文件
2. 将文件放入服务器`plugins/`目录
3. 重启服务器
4. 首次运行后修改`config.yml`进行配置

## 🎮 命令列表

（包含以下6个核心命令）

```
/sbw add <世界名>      ➕ 添加世界到白名单
/sbw remove <世界名>   ➖ 从白名单移除世界
/sbw list            📜 查看白名单列表
/sbw reload          🔄 重载配置文件
/sbw help            ❓ 查看帮助信息
/sbw tp <世界名>     ➡️ 传送至指定世界
```

## 🔑 权限节点

- `starbanworld.admin` - 管理命令权限 (默认OP)
- `starbanworld.world.<世界名>` - 指定世界访问权限
- `starbanworld.teleport` - 使用/sbw tp命令传送至白名单世界

## ⚙️ 配置文件示例
```yaml
whitelist:
  - world
  - world_nether

messages:
  no-permission: "&c⚠️ 警告：您没有进入 %world% 世界的权限！"
```

## 📄 开源协议
本项目采用 [MIT License](LICENSE)

## 🎮 命令教程

### 世界传送命令

```
/sbw tp <世界名>  ➡️ 传送至指定世界（需要starbanworld.teleport权限）
```

- 应用场景：允许普通玩家在拥有权限时传送到已授权的世界
- 权限检查：同时验证目标世界是否在白名单或拥有对应世界权限