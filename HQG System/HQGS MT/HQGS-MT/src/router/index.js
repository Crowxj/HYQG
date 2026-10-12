import { createRouter, createWebHistory } from 'vue-router'
// 引入页面
import Login from '@/views/LoginSys.vue'
import Register from '@/views/RegisterId.vue'
import ForgetPwd from '@/views/ForgetPwd.vue'
import Dashboard from '@/views/Dashboard.vue'
// 引入大屏
import ScreenView from '@/views/screen/ScreenView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/dashboard',
      children: [
        {
          path: 'login',
          name: 'Login',
          component: Login,
        },
        {
          path: 'register',
          name: 'Register',
          component: Register,
        },
        {
          path: 'forgetPwd',
          name: 'ForgetPwd',
          component: ForgetPwd,
        },
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: Dashboard,
        },
      ],
    },
    // =========大屏路由 =========
    {
      path: '/display',
      name: 'ScreenDisplay',
      component: ScreenView,
    },
    // 根路径直接跳转到登录页
    {
      path: '/',
      redirect: '/dashboard/login',
    },
  ],
})

// 全局路由守卫：修复后退bug
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  // 已登录（有token），访问登录/注册/忘记密码页面 → 强制跳仪表盘
  if (token) {
    if (['/dashboard/login', '/dashboard/register', '/dashboard/forgetPwd'].includes(to.path)) {
      return next('/dashboard')
    }
  } else {
    // 没有token，访问仪表盘 → 强制跳登录；大屏display不需要token直接放行
    if (to.path === '/dashboard') {
      return next('/dashboard/login')
    }
  }
  next()
})

export default router
