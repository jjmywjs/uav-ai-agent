# 航空货运无人机 AI 智能体 SkyBot - 前端项目

这是一个基于 Vue3 的前端项目，用于与航空货运无人机 AI 智能体系统进行交互。

## 项目描述

SkyBot 是一个集成多个 AI 智能体的应用平台，包括：
- **航空货运无人机智能专家**：针对无人机应用优化的 AI 智能体
- **AI 超级智能体应用**：集成多种能力的超级智能体

## 技术栈

- Vue 3
- Vue Router 4
- Axios
- Vite
- UUID

## 项目结构

```
src/
├── components/          # 可复用组件
├── pages/              # 页面组件
│   ├── Home.vue       # 主页 - 应用选择器
│   ├── UavApp.vue     # 航空货运无人机智能专家
│   └── ManusApp.vue   # AI 超级智能体应用
├── router/
│   └── index.js       # 路由配置
├── services/
│   └── api.js         # API 服务
├── App.vue            # 主应用组件
├── main.js            # 应用入口
└── style.css          # 全局样式

index.html             # HTML 模板
vite.config.js         # Vite 配置
package.json           # 项目依赖配置
```

## 安装与运行

### 1. 安装依赖
```bash
npm install
```

### 2. 启动开发服务器
```bash
npm run dev
```

访问 `http://localhost:5173` 查看应用。

### 3. 构建生产版本
```bash
npm run build
```

## 功能特性

### 主页 (Home)
- 应用选择界面
- 两个应用入口卡片
- 响应式设计

### 航空货运无人机智能专家 (UavApp)
- 聊天界面设计
- 自动生成聊天室 ID (UUID)
- 基于 SSE（Server-Sent Events）的实时消息流
- 支持流式接收 AI 响应
- 聊天消息显示（用户消息在右，AI 消息在左）
- 时间戳显示
- 加载状态指示

### AI 超级智能体应用 (ManusApp)
- 同 UavApp 相似的聊天界面
- 支持不同的后端接口
- 连接状态指示
- 优化的消息处理

## API 接口配置

### 后端地址
```
http://localhost:8123/api
```

### 可用接口

#### 1. 航空货运无人机智能专家 SSE 接口
- **URL**: `/ai/uav_app/chat/sse`
- **方法**: GET
- **参数**: 
  - `message` (string): 用户消息
  - `chatId` (string): 聊天室 ID
- **返回**: Server-Sent Events 流

#### 2. AI 超级智能体应用接口
- **URL**: `/ai/manus/chat`
- **方法**: GET
- **参数**:
  - `message` (string): 用户消息
- **返回**: Server-Sent Events 流

## 使用说明

### 1. 进入应用
访问首页，选择要使用的应用入口。

### 2. 开始对话
- 在输入框中输入您的问题或指令
- 按 Enter 或点击"发送"按钮
- AI 会实时流式返回响应

### 3. 返回主页
点击左上角的"返回"按钮返回应用选择页面。

## 特别说明

### SSE 连接处理
- 应用使用浏览器原生 EventSource API 连接 SSE
- 自动处理连接错误和重新连接
- 支持 JSON 和纯文本消息格式
- 识别 `[DONE]` 消息作为流结束标记

### 消息处理
- 支持流式消息拼接（在单个消息气泡中累积）
- 自动滚动到最新消息
- 显示打字动画加载状态

### 响应式设计
- 支持桌面和移动设备
- 自适应布局

## 开发注意事项

### 环境配置
确保后端服务运行在 `http://localhost:8123`

### 跨域问题
如果遇到 CORS 错误，请检查后端是否正确配置了 CORS。

### 浏览器兼容性
- 需要支持 EventSource API 的现代浏览器
- 建议使用最新版本的 Chrome、Firefox、Safari 或 Edge

## 许可证

MIT
