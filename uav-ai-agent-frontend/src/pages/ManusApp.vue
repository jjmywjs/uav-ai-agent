<template>
  <div class="chat-container">
    <!-- Header -->
    <div class="chat-header">
      <div class="header-content">
        <button class="back-btn" @click="goBack">
          ← 返回
        </button>
        <h1>AI 超级智能体应用</h1>
        <div class="chat-info">
          <span class="status-badge">{{ connectionStatus }}</span>
        </div>
      </div>
    </div>

    <!-- Chat Messages -->
    <div class="chat-messages" ref="messagesContainer">
      <div v-if="messages.length === 0" class="empty-state">
        <div class="empty-icon">✨</div>
        <p>与超级智能体对话，获取强大的AI支持！</p>
      </div>
      
      <div 
        v-for="(message, index) in messages" 
        :key="index"
        :class="['message-group', message.role]"
      >
        <div class="message-avatar">
          {{ message.role === 'user' ? '👤' : '✨' }}
        </div>
        <div class="message-content">
          <div class="message-bubble">{{ message.content }}</div>
          <div class="message-time">{{ formatTime(message.timestamp) }}</div>
        </div>
      </div>

      <!-- Loading indicator -->
      <div v-if="loading" class="message-group ai">
        <div class="message-avatar">✨</div>
        <div class="message-content">
          <div class="message-bubble typing">
            <span></span><span></span><span></span>
          </div>
        </div>
      </div>
    </div>

    <!-- Input Area -->
    <div class="chat-input-area">
      <div class="input-wrapper">
        <input
          v-model="inputMessage"
          type="text"
          placeholder="输入您的问题或指令..."
          @keyup.enter="sendMessage"
          :disabled="loading"
          class="message-input"
        />
        <button
          @click="sendMessage"
          :disabled="loading || !inputMessage.trim()"
          class="send-btn"
        >
          {{ loading ? '处理中...' : '发送' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { chatWithManus } from '../services/api.js'

export default {
  name: 'ManusApp',
  data() {
    return {
      messages: [],
      inputMessage: '',
      loading: false,
      eventSource: null,
      connectionStatus: '就绪'
    }
  },
  beforeUnmount() {
    // Close EventSource when component is destroyed
    if (this.eventSource) {
      this.eventSource.close()
    }
  },
  methods: {
    sendMessage() {
      if (!this.inputMessage.trim() || this.loading) {
        return
      }

      const userMessage = this.inputMessage
      this.inputMessage = ''

      // Add user message
      this.messages.push({
        role: 'user',
        content: userMessage,
        timestamp: new Date()
      })

      // Set loading state
      this.loading = true
      this.connectionStatus = '处理中...'
      this.scrollToBottom()

      // Get AI response via SSE
      this.getAIResponse(userMessage)
    },

    getAIResponse(message) {
      try {
        // Create EventSource
        this.eventSource = chatWithManus(message)

        let aiResponse = ''
        let messageIndex = null

        this.eventSource.onopen = () => {
          console.log('SSE connection opened for Manus')
          this.connectionStatus = '已连接'
        }

        this.eventSource.onmessage = (event) => {
          const data = event.data

          // Check if it's a completion message
          if (data === '[DONE]' || data.includes('done') || data === ':heartbeat') {
            if (data !== ':heartbeat') {
              this.eventSource.close()
              this.loading = false
              this.connectionStatus = '就绪'
            }
            this.scrollToBottom()
            return
          }

          // Parse data
          try {
            // Try parsing as JSON
            const parsed = JSON.parse(data)
            const chunk = parsed.content || parsed.message || parsed.data || ''

            if (chunk && chunk !== ':heartbeat') {
              aiResponse += chunk

              // Add or update AI message
              if (messageIndex === null) {
                this.messages.push({
                  role: 'ai',
                  content: chunk,
                  timestamp: new Date()
                })
                messageIndex = this.messages.length - 1
              } else {
                this.messages[messageIndex].content = aiResponse
              }

              this.scrollToBottom()
            }
          } catch (e) {
            // If not JSON, treat as plain text
            if (data && data !== ':heartbeat' && !data.startsWith(':')) {
              aiResponse += data

              if (messageIndex === null) {
                this.messages.push({
                  role: 'ai',
                  content: data,
                  timestamp: new Date()
                })
                messageIndex = this.messages.length - 1
              } else {
                this.messages[messageIndex].content = aiResponse
              }

              this.scrollToBottom()
            }
          }
        }

        this.eventSource.onerror = (error) => {
          console.error('SSE error:', error)
          this.eventSource.close()
          this.loading = false
          this.connectionStatus = '就绪'

          if (aiResponse === '') {
            this.messages.push({
              role: 'ai',
              content: '连接出错，请稍后重试',
              timestamp: new Date()
            })
          }

          this.scrollToBottom()
        }
      } catch (error) {
        console.error('Error:', error)
        this.loading = false
        this.connectionStatus = '就绪'
        this.messages.push({
          role: 'ai',
          content: '发生错误，请稍后重试',
          timestamp: new Date()
        })
        this.scrollToBottom()
      }
    },

    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer
        if (container) {
          container.scrollTop = container.scrollHeight
        }
      })
    },

    formatTime(date) {
      if (!(date instanceof Date)) {
        date = new Date(date)
      }
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      return `${hours}:${minutes}`
    },

    goBack() {
      if (this.eventSource) {
        this.eventSource.close()
      }
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.chat-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.chat-header {
  background: linear-gradient(135deg, #00aa66 0%, #008844 100%);
  color: white;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 20px;
}

.back-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.95rem;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.chat-header h1 {
  flex-grow: 1;
  font-size: 1.5rem;
  font-weight: 600;
}

.chat-info {
  font-size: 0.9rem;
  opacity: 0.9;
}

.status-badge {
  background: rgba(255, 255, 255, 0.1);
  padding: 4px 12px;
  border-radius: 20px;
  display: inline-block;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
  width: 100%;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  opacity: 0.6;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 10px;
}

.empty-state p {
  color: #666;
  font-size: 1rem;
}

.message-group {
  display: flex;
  margin-bottom: 16px;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message-group.user {
  justify-content: flex-end;
}

.message-group.ai {
  justify-content: flex-start;
}

.message-avatar {
  font-size: 1.5rem;
  margin: 0 10px;
  flex-shrink: 0;
}

.message-group.user .message-avatar {
  order: 2;
  margin: 0 10px 0 0;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.message-group.user .message-content {
  align-items: flex-end;
}

.message-group.ai .message-content {
  align-items: flex-start;
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 12px;
  word-wrap: break-word;
  line-height: 1.5;
  font-size: 0.95rem;
}

.message-group.user .message-bubble {
  background: linear-gradient(135deg, #00aa66 0%, #008844 100%);
  color: white;
  border-bottom-right-radius: 2px;
}

.message-group.ai .message-bubble {
  background: white;
  color: #333;
  border: 1px solid #e0e0e0;
  border-bottom-left-radius: 2px;
}

.message-bubble.typing {
  display: flex;
  gap: 4px;
  align-items: center;
  padding: 12px 16px;
  min-width: 50px;
  justify-content: center;
}

.message-bubble.typing span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #999;
  animation: typing 1.4s infinite;
}

.message-bubble.typing span:nth-child(2) {
  animation-delay: 0.2s;
}

.message-bubble.typing span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    opacity: 0.5;
    transform: translateY(0);
  }
  30% {
    opacity: 1;
    transform: translateY(-10px);
  }
}

.message-time {
  font-size: 0.75rem;
  color: #999;
  margin-top: 4px;
}

.message-group.user .message-time {
  text-align: right;
}

.message-group.ai .message-time {
  text-align: left;
}

.chat-input-area {
  background: white;
  border-top: 1px solid #e0e0e0;
  padding: 20px;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);
}

.input-wrapper {
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  gap: 10px;
}

.message-input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 0.95rem;
  outline: none;
  transition: all 0.3s ease;
}

.message-input:focus {
  border-color: #00aa66;
  box-shadow: 0 0 0 3px rgba(0, 170, 102, 0.1);
}

.message-input:disabled {
  background-color: #f5f5f5;
  color: #999;
}

.send-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #00aa66 0%, #008844 100%);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 0.95rem;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.send-btn:hover:not(:disabled) {
  box-shadow: 0 4px 12px rgba(0, 170, 102, 0.4);
  transform: translateY(-2px);
}

.send-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .header-content {
    flex-wrap: wrap;
    gap: 10px;
  }

  .chat-header h1 {
    width: 100%;
    order: 2;
  }

  .chat-messages {
    padding: 15px;
  }

  .message-group {
    margin-bottom: 12px;
  }

  .message-content {
    max-width: 85%;
  }

  .input-wrapper {
    flex-direction: column;
  }

  .send-btn {
    width: 100%;
  }
}
</style>
