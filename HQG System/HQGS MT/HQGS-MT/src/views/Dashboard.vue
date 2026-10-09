<template>
  <div class="dashboard-page">
    <!--顶部导航栏 -->
    <HeaderBar
      :username="username"
      @toggle-sidebar="showSidebar = !showSidebar"
      @logout="handleLogout"
    />
    <!--侧边栏 -->
    <SideBar :show-sidebar="showSidebar" :active-menu="activeMenu" @menu-click="handleMenuClick" />
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import HeaderBar from '../components/HeaderBar.vue'
import SideBar from '../components/SideBar.vue'
const router = useRouter()
const showSidebar = ref(false)
const activeMenu = ref('/admin/dashboard')

const username = computed(() => {
  const rawName = localStorage.getItem('username') || ''
  return rawName.includes('@') ? rawName.split('@')[0] : rawName
})
//菜单点击
const handleMenuClick = (path) => {
  activeMenu.value = path
  router.push(path)
}
// 退出登录核心方法
const handleLogout = () => {
  if (confirm('确定退出登录吗？')) {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    router.push('/login')
  }
}
</script>
<style>
.dashboard-page {
  width: 100%;
  height: 100vh;
}
</style>
