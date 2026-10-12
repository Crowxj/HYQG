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
    // =========管理端父路由 前缀 /dashboard =========
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
          path: '',
          name: 'Dashboard',
          component: Dashboard,
        },
      ],
    },
    // =========大屏预留路由，页面后续再补 =========
    {
      path: '/display',
      name: 'ScreenDisplay',
      // component: ScreenDisplay 后续写好大屏页面再解开注释
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
  // 其他页面正常放行
  next()
})

export default router
