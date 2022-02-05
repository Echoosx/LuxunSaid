# LuxunSaid

> 基于 [Mirai Console](https://github.com/mamoe/mirai-console) 的 表情包生成插件

[![Release](https://img.shields.io/github/v/release/Echoosx/LuxunSaid)](https://github.com/Echoosx/LuxunSaid/releases)
[![Build](https://github.com/Echoosx/LuxunSaid/workflows/Java%20CI%20with%20Gradle/badge.svg?branch=master)](https://github.com/Echoosx/LuxunSaid/actions/workflows/gradle.yml)

## 功能
使用指令`<鲁迅说> [content]`就能获得一张表情包（支持内容中有空格）
![img.png](static/img.png)

## 指令
注意: 使用前请确保可以 [在聊天环境执行指令](https://github.com/project-mirai/chat-command)  
带括号的`/`前缀是缺省的  
`<...>`中的是指令名，由`空格`隔开表示其中任一名称都可执行  
`[...]`表示参数，当`[...]`后面带`?`时表示参数可缺省
`{...}`表示连续的多个参数


| 指令                   | 描述             |
|:---------------------|:---------------|
| `(/)<lxs 鲁迅说> [xxx]` | 获得鲁迅说`xxx`的表情包 |

## 安装
- 从 [Releases](https://github.com/Echoosx/LuxunSaid/releases) 下载`jar`包，将其放入工作目录下`plugins`文件夹
- 如果没有`plugins`文件夹，先运行 [Mirai Console](https://github.com/mamoe/mirai-console) ，会自动生成
