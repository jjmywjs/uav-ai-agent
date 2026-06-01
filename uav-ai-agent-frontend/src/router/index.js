import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Home.vue'
import UavApp from '../pages/UavApp.vue'
import ManusApp from '../pages/ManusApp.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/uav',
    name: 'UavApp',
    component: UavApp
  },
  {
    path: '/manus',
    name: 'ManusApp',
    component: ManusApp
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
