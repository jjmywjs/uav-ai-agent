import axios from 'axios'

// 根据环境变量设置 API 基础 URL
const API_BASE_URL = process.env.NODE_ENV === 'production' 
 ? '/api' // 生产环境使用相对路径，适用于前后端部署在同一域名下
 : 'http://localhost:8123/api' // 开发环境指向本地后端服务

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000
})

/**
 * 调用航空货运无人机智能应用 SSE 接口
 * @param {string} message - 用户消息
 * @param {string} chatId - 聊天室 ID
 * @returns {EventSource}
 */
export function chatWithUavAppSSE(message, chatId) {
  const url = `${API_BASE_URL}/ai/uav_app/chat/sse?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`
  return new EventSource(url)
}

/**
 * 调用 AI 超级智能体应用接口
 * @param {string} message - 用户消息
 * @returns {EventSource}
 */
export function chatWithManus(message) {
  const url = `${API_BASE_URL}/ai/manus/chat?message=${encodeURIComponent(message)}`
  return new EventSource(url)
}

export default apiClient
