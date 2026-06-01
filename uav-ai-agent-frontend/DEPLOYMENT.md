# 项目部署与使用指南

## 快速开始

### 前置条件
- Node.js 14+ 版本
- npm 或 yarn
- 后端服务运行在 `http://localhost:8123`

### 1. 开发环境设置

```bash
# 进入项目目录
cd uav-ai-agent-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

开发服务器启动后，访问 `http://localhost:5173` 即可使用应用。

### 2. 生产环境构建

```bash
# 构建生产版本
npm run build

# 预览构建结果
npm run preview
```

生成的静态文件会在 `dist` 目录中。

## 项目文件说明

### 核心文件
- **App.vue** - 应用主组件，包含路由视图
- **main.js** - 应用入口，初始化 Vue 和路由
- **index.html** - HTML 模板文件

### 页面组件 (src/pages/)
- **Home.vue** (380 行)
  - 主页，展示两个应用选择卡片
  - 美观的渐变背景设计
  - 响应式卡片布局

- **UavApp.vue** (450+ 行)
  - 航空货运无人机智能专家聊天页面
  - 基于 UUID 生成唯一的聊天室 ID
  - SSE 实时流式消息接收
  - 完整的聊天界面：消息气泡、时间戳、加载状态

- **ManusApp.vue** (480+ 行)
  - AI 超级智能体应用聊天页面
  - 功能与 UavApp 类似
  - 针对不同的后端接口优化
  - 连接状态显示

### 服务层 (src/services/)
- **api.js** (33 行)
  - API 服务模块
  - 导出 `chatWithUavAppSSE()` 函数
  - 导出 `chatWithManus()` 函数
  - 配置后端基础 URL

### 路由配置 (src/router/)
- **index.js** (24 行)
  - 定义三个路由：Home、UavApp、ManusApp
  - 路由映射关系

### 样式文件
- **style.css** - 全局样式和滚动条美化

### 配置文件
- **package.json** - 项目依赖和脚本定义
- **vite.config.js** - Vite 构建工具配置
- **.gitignore** - Git 忽略规则

## 功能模块详解

### 1. 主页功能
- 显示项目标题和副标题
- 两个应用选择卡片
- 卡片悬停动画效果
- 点击卡片导航到对应应用

### 2. 聊天功能（通用）
所有聊天页面都包含以下功能：

#### 消息显示
- 用户消息显示在右侧（蓝色）
- AI 消息显示在左侧（白色）
- 每条消息包含时间戳
- 支持长文本自动换行

#### 实时流式接收
- 使用浏览器 EventSource API
- 自动处理 JSON 和纯文本格式
- 支持多种消息格式解析
- 在单个气泡中累积消息

#### 加载状态
- 显示打字动画
- 禁用输入和发送按钮
- 显示处理状态提示

#### 错误处理
- 网络错误时显示错误提示
- 自动关闭 EventSource 连接

### 3. UAV 应用特性
- 自动生成 UUID 作为聊天室 ID
- 显示聊天 ID 供用户参考
- 每次进入页面生成新的聊天室 ID

### 4. Manus 应用特性
- 显示实时连接状态（就绪/处理中/已连接）
- 心跳消息处理（`:heartbeat`）
- 针对服务端的流结束标记处理

## API 接口交互

### SSE 连接流程

```javascript
// 1. 创建 EventSource 连接
const eventSource = new EventSource(url)

// 2. 监听消息事件
eventSource.onmessage = (event) => {
  const data = event.data
  // 处理消息...
}

// 3. 处理错误
eventSource.onerror = (error) => {
  // 处理错误...
}

// 4. 关闭连接
eventSource.close()
```

### 消息格式处理

应用支持以下消息格式：
```json
// 格式 1：标准 JSON
{"content": "message text"}
{"message": "message text"}

// 格式 2：纯文本
plain text message

// 格式 3：特殊标记
[DONE]           // 流结束标记
:heartbeat       // 心跳信号
```

## 调试技巧

### 1. 浏览器控制台
- 打开 DevTools (F12)
- 查看 Network 标签监控 SSE 连接
- 查看 Console 标签查看错误信息

### 2. 常见问题
- **无法连接到后端**：检查后端服务是否运行在 8123 端口
- **SSE 连接断开**：检查浏览器控制台是否有错误
- **消息显示不完整**：检查消息格式是否正确

## 性能优化建议

1. **代码分割** - 为每个路由组件启用懒加载
2. **图片优化** - 使用更小的图片和 WebP 格式
3. **缓存策略** - 配置 HTTP 缓存头
4. **压缩** - 启用 gzip 压缩

## 扩展方向

1. **增加更多应用** - 在 Home.vue 中添加更多卡片
2. **消息历史** - 添加本地存储保存聊天记录
3. **主题切换** - 实现亮色/暗色主题
4. **离线支持** - 添加 Service Worker 支持
5. **用户认证** - 添加登录/注册功能

## 故障排查

### 项目不能启动
```bash
# 1. 清除 node_modules
rm -rf node_modules
npm install

# 2. 清除缓存
npm cache clean --force

# 3. 重新启动
npm run dev
```

### 构建失败
```bash
# 检查依赖是否完整
npm list

# 尝试使用最新的 Node 版本
node --version

# 重新构建
npm run build
```

## 部署到服务器

### 使用 Nginx
```nginx
server {
    listen 80;
    server_name example.com;
    
    location / {
        root /path/to/dist;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8123/api;
    }
}
```

### 使用 Docker
创建 Dockerfile：
```dockerfile
FROM node:18
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build
EXPOSE 3000
CMD ["npm", "run", "preview"]
```

## 环境变量配置

创建 `.env` 文件：
```
VITE_API_URL=http://localhost:8123/api
```

在代码中使用：
```javascript
const apiUrl = import.meta.env.VITE_API_URL
```

## 许可证
MIT
