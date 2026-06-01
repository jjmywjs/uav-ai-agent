<template>
  <div class="home-container">
    <div class="home-content">
      <div class="header">
        <h1 class="title">航空货运无人机 AI 智能体</h1>
        <p class="subtitle">SkyBot</p>
      </div>

      <div class="description">
        <p>选择一个应用开始使用智能体服务</p>
      </div>

      <div class="apps-grid">
        <div class="app-card" @click="goToApp('uav')">
          <div class="app-icon">
            <svg width="60" height="60" viewBox="0 0 60 60" fill="none">
              <!-- 无人机机身 -->
              <circle cx="30" cy="25" r="6" fill="#0066cc"/>
              <!-- 四个螺旋桨臂 -->
              <line x1="30" y1="25" x2="30" y2="8" stroke="#0066cc" stroke-width="2.5"/>
              <line x1="30" y1="25" x2="47" y2="25" stroke="#0066cc" stroke-width="2.5"/>
              <line x1="30" y1="25" x2="30" y2="42" stroke="#0066cc" stroke-width="2.5"/>
              <line x1="30" y1="25" x2="13" y2="25" stroke="#0066cc" stroke-width="2.5"/>
              <!-- 螺旋桨 -->
              <ellipse cx="30" cy="8" rx="5" ry="2.5" fill="#0066cc" opacity="0.6"/>
              <ellipse cx="47" cy="25" rx="2.5" ry="5" fill="#0066cc" opacity="0.6"/>
              <ellipse cx="30" cy="42" rx="5" ry="2.5" fill="#0066cc" opacity="0.6"/>
              <ellipse cx="13" cy="25" rx="2.5" ry="5" fill="#0066cc" opacity="0.6"/>
              <!-- 相机镜头 -->
              <circle cx="30" cy="30" r="3" fill="none" stroke="#0066cc" stroke-width="1.5"/>
              <!-- 信号指示 -->
              <path d="M 42 48 Q 44 46 46 44" stroke="#0066cc" stroke-width="1.5" fill="none"/>
              <path d="M 44 50 Q 47 46 50 42" stroke="#0066cc" stroke-width="1" fill="none" opacity="0.6"/>
            </svg>
          </div>
          <h2 class="app-name">航空货运无人机智能专家</h2>
          <p class="app-description">专门为航空货运无人机应用优化的 AI 智能体</p>
          <div class="app-action">进入应用 →</div>
        </div>

        <div class="app-card" @click="goToApp('manus')">
          <div class="app-icon">
            <svg width="60" height="60" viewBox="0 0 60 60" fill="none">
              <!-- 脑电波背景 -->
              <path d="M 12 30 Q 15 25 18 30 Q 21 35 24 30" stroke="#00aa66" stroke-width="1.5" fill="none" opacity="0.4"/>
              <path d="M 36 30 Q 39 25 42 30 Q 45 35 48 30" stroke="#00aa66" stroke-width="1.5" fill="none" opacity="0.4"/>
              <!-- 大脑主体 -->
              <ellipse cx="30" cy="28" rx="12" ry="14" fill="#00aa66" opacity="0.2" stroke="#00aa66" stroke-width="1.5"/>
              <!-- 大脑波纹 -->
              <path d="M 20 20 Q 22 18 24 20" stroke="#00aa66" stroke-width="2" fill="none"/>
              <path d="M 36 20 Q 38 18 40 20" stroke="#00aa66" stroke-width="2" fill="none"/>
              <path d="M 20 35 Q 22 37 24 35" stroke="#00aa66" stroke-width="2" fill="none"/>
              <path d="M 36 35 Q 38 37 40 35" stroke="#00aa66" stroke-width="2" fill="none"/>
              <!-- 中心芯片 -->
              <circle cx="30" cy="28" r="4" fill="none" stroke="#00aa66" stroke-width="1.5"/>
              <line x1="28" y1="28" x2="32" y2="28" stroke="#00aa66" stroke-width="1"/>
              <line x1="30" y1="26" x2="30" y2="30" stroke="#00aa66" stroke-width="1"/>
              <!-- 闪烁点 -->
              <circle cx="26" cy="24" r="2" fill="#00aa66"/>
              <circle cx="34" cy="24" r="2" fill="#00aa66"/>
              <circle cx="26" cy="35" r="2" fill="#00aa66"/>
              <circle cx="34" cy="35" r="2" fill="#00aa66"/>
            </svg>
          </div>
          <h2 class="app-name">AI 超级智能体应用</h2>
          <p class="app-description">集成多种能力的超级智能体应用</p>
          <div class="app-action">进入应用 →</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    goToApp(appName) {
      this.$router.push(`/${appName}`)
    }
  }
}
</script>

<style scoped>
.home-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.home-content {
  width: 100%;
  max-width: 1200px;
  padding: 40px 20px;
  text-align: center;
}

.header {
  margin-bottom: 40px;
  color: white;
}

.title {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 10px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.subtitle {
  font-size: 2rem;
  font-weight: 300;
  opacity: 0.95;
}

.description {
  margin-bottom: 50px;
  color: rgba(255, 255, 255, 0.9);
  font-size: 1.1rem;
}

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 30px;
  margin-top: 40px;
}

.app-card {
  background: white;
  border-radius: 12px;
  padding: 40px 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.app-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.15);
}

.app-icon {
  margin-bottom: 25px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-10px);
  }
}

.app-name {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 12px;
  font-weight: 600;
}

.app-description {
  color: #666;
  font-size: 0.95rem;
  line-height: 1.6;
  margin-bottom: 25px;
  flex-grow: 1;
}

.app-action {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 12px 24px;
  border-radius: 6px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.app-card:hover .app-action {
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
}

@media (max-width: 768px) {
  .title {
    font-size: 2rem;
  }

  .subtitle {
    font-size: 1.5rem;
  }

  .apps-grid {
    grid-template-columns: 1fr;
  }

  .app-card {
    padding: 30px 20px;
  }
}
</style>
