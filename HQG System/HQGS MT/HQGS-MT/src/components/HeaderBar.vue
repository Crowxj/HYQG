<template>
  <header class="dashboard-header">
    <h1>
      <button class="menu-btn" @click="$emit('toggle-sidebar')">
       <i class="iconfont">&#xe64f;</i>
      </button>
      <!-- <img class="system-logo" src="@/assets/images/login_logo.webp" alt="logo"> -->
      <span>擎工可视化系统</span>
    </h1>
    <div class="user-info">
      <span>Hello，{{ username }}</span>
      <button @click="$emit('logout')" class="logout-btn">
        <i class="iconfont">&#xe6a1;</i>
      </button>
    </div>
  </header>
</template>
<script>
export default {
  name: "HeaderBar",
  props: {
    username: {
      type: String,
      default: ""
    }
  },
  emits: ["toggle-sidebar", "logout"]
}
</script>
<style scoped>
.dashboard-header {
  background: #0A2463;
  height: 47px;
  padding: 0 40px;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: none;
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 101;
}
.dashboard-header h1 {
  margin: 0;
  font-size: 18px;
  color: #ffffff;
  display: flex;
  align-items: center;
  gap: 14px;
}
.header-icon {
  font-size: 24px;
  color: #ffffff;
}
.menu-btn {
  border: none;
  background: rgba(255, 255, 255, 0.2);
  width: 25px;
  height: 25px;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: -20px;
}
.menu-btn :deep(.iconfont) {
  color: #fff;
  font-size: 14px;
}
.menu-btn:hover {
  background: rgba(255, 255, 255, 0.35);
}
.iconfont {
  font-size: 20px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
  color: #ffffff;
}
.logout-btn {
  width: 25px;
  height: 25px;
  margin-left: 20px;
  background: transparent;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s;
}
.logout-btn:hover {
  background: transparent;
}
.logout-btn:active :deep(.iconfont) {
  color: #ff3333;
}
.logout-btn :deep(.iconfont) {
  font-size: 24px;
}
/* logo样式 */
.system-logo {
  height: 20px;
  width: auto;
}
</style>
