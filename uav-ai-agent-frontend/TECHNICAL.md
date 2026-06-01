# 技术实现文档

## 架构概览

```
┌─────────────────────────────────────────────┐
│           浏览器 (Frontend)                  │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │      Vue 3 Application              │   │
│  │                                     │   │
│  │  ┌──────────────┐  ┌─────────────┐ │   │
│  │  │   Router     │  │  Pages      │ │   │
│  │  │  - Home      │  │ - Home      │ │   │
│  │  │  - UavApp    │  │ - UavApp    │ │   │
│  │  │  - ManusApp  │  │ - ManusApp  │ │   │
│  │  └──────────────┘  └─────────────┘ │   │
│  │         │               │           │   │
│  │         └───────┬───────┘           │   │
│  │                 │                   │   │
│  │          ┌──────▼──────┐            │   │
│  │          │   Services  │            │   │
│  │          │   (api.js)  │            │   │
│  │          └──────┬──────┘            │   │
│  └───────────────────────────────────────┘ │
│                    │                       │
│            SSE EventSource API             │
└────────────────────┼──────────────────────┘
                     │
                     │ HTTP EventStream
                     ▼
┌─────────────────────────────────────────────┐
│       Spring Boot Backend Server            │
│      (localhost:8123/api)                   │
│                                             │
│  ┌─────────────────────────────────────┐   │
│  │       AiController                  │   │
│  │  - doChatWithUavAppSSE()           │   │
│  │  - doChatWithManus()               │   │
│  └─────────────────────────────────────┘   │
│                 │        │                 │
│      ┌──────────┘        └──────────┐      │
│      ▼                             ▼      │
│  ┌─────────────────┐      ┌──────────────┐│
│  │  UavApp Service │      │ UavManus     ││
│  │ (AI处理逻辑)     │      │ Service      ││
│  └─────────────────┘      └──────────────┘│
└─────────────────────────────────────────────┘
```

## 核心模块说明

### 1. 路由系统 (src/router/index.js)

```javascript
const routes = [
  {
    path: '/',           // 主页
    component: Home      // Home.vue
  },
  {
    path: '/uav',        // 无人机应用
    component: UavApp    // UavApp.vue
  },
  {
    path: '/manus',      // 超级智能体应用
    component: ManusApp  // ManusApp.vue
  }
]
```

### 2. API 服务层 (src/services/api.js)

#### EventSource 创建
```javascript
export function chatWithUavAppSSE(message, chatId) {
  const url = `http://localhost:8123/api/ai/uav_app/chat/sse?...`
  return new EventSource(url)
}
```

**特点**：
- 使用原生浏览器 EventSource API
- 支持 SSE (Server-Sent Events) 流式数据
- URL 参数进行 encodeURIComponent 编码以处理特殊字符

### 3. 主页组件 (src/pages/Home.vue)

#### 结构
```
<div class="home-container">
  <header>标题和副标题</header>
  <grid>
    <card>应用1</card>
    <card>应用2</card>
  </grid>
</div>
```

#### 关键功能
- 渐变背景：`linear-gradient(135deg, #667eea 0%, #764ba2 100%)`
- 卡片悬停动画：`transform: translateY(-8px)`
- 响应式栅格：`grid-template-columns: repeat(auto-fit, minmax(350px, 1fr))`
- 路由导航：`this.$router.push(/appName)`

### 4. 聊天组件基础设计

两个聊天页面 (UavApp.vue & ManusApp.vue) 采用相似的架构：

#### 数据结构
```javascript
data() {
  return {
    messages: [
      {
        role: 'user',      // 或 'ai'
        content: 'text',   // 消息内容
        timestamp: Date    // 时间戳
      }
    ],
    inputMessage: '',      // 输入框内容
    loading: false,        // 加载状态
    eventSource: null,     // SSE 连接
    // UAV特定：
    chatId: '',           // 聊天室 ID (UUID)
    // MANUS特定：
    connectionStatus: ''  // 连接状态
  }
}
```

#### 生命周期钩子
```javascript
mounted() {
  // 初始化 - UAV 应用生成 UUID
  this.chatId = uuidv4()
}

beforeUnmount() {
  // 清理资源 - 关闭 SSE 连接
  if (this.eventSource) {
    this.eventSource.close()
  }
}
```

### 5. SSE 消息处理流程

#### UAV 应用消息处理
```javascript
const aiResponse = ''    // 累积消息
let messageIndex = null  // 消息索引

eventSource.onmessage = (event) => {
  if (event.data === '[DONE]') {
    // 流结束
    eventSource.close()
    loading = false
    return
  }

  try {
    // 尝试解析 JSON
    const parsed = JSON.parse(event.data)
    const chunk = parsed.content || ''
    
    if (messageIndex === null) {
      // 创建新消息
      messages.push({ role: 'ai', content: chunk })
      messageIndex = messages.length - 1
    } else {
      // 追加到已有消息
      messages[messageIndex].content = aiResponse
    }
  } catch {
    // 纯文本处理...
  }
}
```

#### Manus 应用消息处理（增强）
```javascript
// 额外处理心跳信号
if (data === ':heartbeat') {
  // 忽略心跳，继续等待
  return
}

// 处理其他格式
const chunk = parsed.data || ''
```

### 6. UI 组件设计

#### 消息气泡样式
```css
/* 用户消息 */
.message-group.user .message-bubble {
  background: linear-gradient(135deg, #0066cc 0%, #0052a3 100%);
  color: white;
  justify-content: flex-end;
}

/* AI 消息 */
.message-group.ai .message-bubble {
  background: white;
  color: #333;
  border: 1px solid #e0e0e0;
}
```

#### 加载动画
```css
.message-bubble.typing span {
  animation: typing 1.4s infinite;
}

@keyframes typing {
  0%, 60%, 100% { opacity: 0.5; transform: translateY(0); }
  30% { opacity: 1; transform: translateY(-10px); }
}
```

#### 响应式布局
```css
@media (max-width: 768px) {
  .message-content {
    max-width: 85%;  /* 移动端更宽 */
  }
  .input-wrapper {
    flex-direction: column;
  }
}
```

### 7. 辅助功能

#### UUID 生成（UAV应用）
```javascript
import { v4 as uuidv4 } from 'uuid'

mounted() {
  this.chatId = uuidv4()
  // 例：550e8400-e29b-41d4-a716-446655440000
}
```

#### 时间格式化
```javascript
formatTime(date) {
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${hours}:${minutes}`  // 输出：14:30
}
```

#### 自动滚动
```javascript
scrollToBottom() {
  this.$nextTick(() => {
    const container = this.$refs.messagesContainer
    if (container) {
      container.scrollTop = container.scrollHeight
    }
  })
}
```

## 错误处理机制

### 1. SSE 连接错误
```javascript
eventSource.onerror = (error) => {
  console.error('SSE error:', error)
  eventSource.close()
  
  if (aiResponse === '') {
    messages.push({
      role: 'ai',
      content: '连接出错，请稍后重试'
    })
  }
}
```

### 2. 消息解析错误
```javascript
try {
  JSON.parse(data)
} catch (e) {
  // 降级处理为纯文本
  aiResponse += data
}
```

### 3. 输入验证
```javascript
sendMessage() {
  if (!this.inputMessage.trim() || this.loading) {
    return  // 空消息或正在加载时拒绝发送
  }
}
```

## 状态管理模式

### 三层状态
```javascript
// 1. 全局应用状态（如需要）
// 可使用 Pinia 或 Vuex 扩展

// 2. 页面级状态
data() {
  return {
    messages: [],
    inputMessage: ''
  }
}

// 3. 组件内临时状态
let messageIndex = null  // 方法内临时变量
```

## 性能考虑

### 1. 虚拟滚动（未实现，可扩展）
对于大量消息，可使用虚拟滚动库（如 `vue-virtual-scroller`）

### 2. 消息缓存
```javascript
// 可添加消息缓存到 localStorage
localStorage.setItem('messages', JSON.stringify(messages))
```

### 3. 防抖/节流
```javascript
// 发送消息前可添加防抖
const sendMessage = debounce(() => { ... }, 300)
```

## 安全性

### 1. 输入 sanitization
```javascript
// 当前：基础的 encodeURIComponent
// 可增强：使用专门的 HTML sanitizer
```

### 2. CORS 处理
```javascript
// 假设后端已配置 CORS
// 如果需要自定义，可在 vite.config.js 中配置代理
```

### 3. XSS 防护
```javascript
// Vue 3 自动转义模板插值，提供基础 XSS 保护
// 避免使用 v-html 显示用户输入
```

## 可扩展性设计

### 1. 添加新应用
- 创建新的页面组件
- 在 router 中添加路由
- 在 Home.vue 中添加卡片

### 2. 自定义样式主题
- 提取颜色常量
- 创建主题配置文件
- 使用 CSS 变量

### 3. 国际化 (i18n)
```javascript
// 可使用 vue-i18n
// 替换硬编码的中文文本
```

## 依赖分析

| 包名 | 用途 | 版本 |
|------|------|------|
| vue | 前端框架 | ^3.3.4 |
| vue-router | 路由管理 | ^4.2.4 |
| axios | HTTP 客户端 | ^1.5.0 |
| uuid | ID 生成 | ^9.0.0 |
| vite | 构建工具 | ^4.4.9 |
| @vitejs/plugin-vue | Vite Vue 插件 | ^4.3.4 |

## 测试建议

### 单元测试
- 测试消息格式化函数
- 测试路由导航

### 集成测试
- 测试 SSE 连接流程
- 测试消息发送和接收

### E2E 测试
- 测试完整的对话流程
- 测试错误恢复

## 部署检查清单

- [ ] 后端服务配置 CORS
- [ ] API 端点正确性验证
- [ ] SSE 连接稳定性测试
- [ ] 大量消息性能测试
- [ ] 浏览器兼容性测试
- [ ] 移动设备响应式测试
- [ ] 生产环境构建验证

