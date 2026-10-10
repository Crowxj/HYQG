import { createRouter, createWebHistory } from 'vue-router'
// 引入页面
import Login from '@/views/LoginSys.vue'
import Register from '@/views/RegisterId.vue'
import ForgetPwd from '@/views/ForgetPwd.vue'
// 新增仪表盘页面
import Dashboard from '@/views/Dashboard.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
    },
    {
      path: '/forgetPwd',
      name: 'ForgetPwd',
      component: ForgetPwd,
    },
    // 仪表盘路由
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: Dashboard,
    },
    // 根路径跳转登录
    {
      path: '/',
      redirect: '/login',
    },
  ],
})
// 全局路由守卫：修复后退bug
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 已登录（有token），访问登录/注册/忘记密码页面 → 强制跳仪表盘
  if (token) {
    if (['/login', '/register', '/forgetPwd'].includes(to.path)) {
      return next('/dashboard')
    }
  } else {
    // 没有token，访问仪表盘 → 强制跳登录
    if (to.path === '/dashboard') {
      return next('/login')
    }
  }
  // 其他页面正常放行
  next()
})
export default router
