<template>
  <div>
    <div v-if="showSidebar" class="sidebar-mask" @click="$emit('close-sidebar')"></div>
    <div class="left-sidebar" :class="{ open: showSidebar }">
      <div class="sidebar-content">
        <div 
          class="sidebar-item" 
          :class="{ active: activeMenu === '/admin/dashboard' }" 
          @click="$emit('menu-click','/admin/dashboard')"
        >
          <span class="iconfont menu-icon">&#xe600;</span>
          <span class="menu-text">看板总览</span>
        </div>

        <!-- 一级菜单：车间管理【带下拉子菜单】 -->
        <div class="sidebar-item-wrap">
          <div 
            class="sidebar-item"
            @click="toggleSubMenu('workshop')"
          >
            <span class="iconfont menu-icon">&#xe603;</span>
            <span class="menu-text">车间管理</span>
            <span class="arrow" :class="{open: currentOpen === 'workshop'}"></span>
          </div>
          <div class="submenu" v-show="currentOpen === 'workshop'">
            <div 
              class="sidebar-item sub-item" 
              :class="{ active: activeMenu === '/admin/workshop/list' }"
              @click="$emit('menu-click','/admin/workshop/list')"
            >
              车间列表
            </div>
            <div 
              class="sidebar-item sub-item" 
              :class="{ active: activeMenu === '/admin/workshop/device' }"
              @click="$emit('menu-click','/admin/workshop/device')"
            >
              设备管理
            </div>
          </div>
        </div>

        <!-- 一级菜单：任务管理【带下拉子菜单】 -->
        <div class="sidebar-item-wrap">
          <div 
            class="sidebar-item"
            @click="toggleSubMenu('task')"
          >
            <span class="iconfont menu-icon">&#xe660;</span>
            <span class="menu-text">任务管理</span>
            <span class="arrow" :class="{open: currentOpen === 'task'}"></span>
          </div>
          <div class="submenu" v-show="currentOpen === 'task'">
            <div 
              class="sidebar-item sub-item" 
              :class="{ active: activeMenu === '/admin/task/list' }"
              @click="$emit('menu-click','/admin/task/list')"
            >
              任务列表
            </div>
            <div 
              class="sidebar-item sub-item" 
              :class="{ active: activeMenu === '/admin/task/add' }"
              @click="$emit('menu-click','/admin/task/add')"
            >
              新增任务
            </div>
          </div>
        </div>

        <!-- 一级菜单：账号管理【带下拉子菜单】 -->
        <div class="sidebar-item-wrap">
          <div 
            class="sidebar-item"
            @click="toggleSubMenu('account')"
          >
            <span class="iconfont menu-icon">&#xe601;</span>
            <span class="menu-text">账号管理</span>
            <span class="arrow" :class="{open: currentOpen === 'account'}"></span>
          </div>
          <div class="submenu" v-show="currentOpen === 'account'">
            <div 
              class="sidebar-item sub-item" 
              :class="{ active: activeMenu === '/admin/account/user' }"
              @click="$emit('menu-click','/admin/account/user')"
            >
              用户列表
            </div>
            <div 
              class="sidebar-item sub-item" 
              :class="{ active: activeMenu === '/admin/account/role' }"
              @click="$emit('menu-click','/admin/account/role')"
            >
              角色权限
            </div>
          </div>
        </div>

        <!-- 一级菜单：个人信息【带下拉子菜单】 -->
        <div class="sidebar-item-wrap">
          <div 
            class="sidebar-item"
            @click="toggleSubMenu('profile')"
          >
            <span class="iconfont menu-icon">&#xeb68;</span>
            <span class="menu-text">个人信息</span>
            <span class="arrow" :class="{open: currentOpen === 'profile'}"></span>
          </div>
          <div class="submenu" v-show="currentOpen === 'profile'">
            <div 
              class="sidebar-item sub-item" 
              :class="{ active: activeMenu === '/admin/profile/base' }"
              @click="$emit('menu-click','/admin/profile/base')"
            >
              基础资料
            </div>
            <div 
              class="sidebar-item sub-item" 
              :class="{ active: activeMenu === '/admin/profile/pwd' }"
              @click="$emit('menu-click','/admin/profile/pwd')"
            >
              修改密码
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "SideBar",
  props: {
    showSidebar: {
      type: Boolean,
      default: false
    },
    activeMenu: {
      type: String,
      default: ""
    }
  },
  emits: ["close-sidebar", "menu-click"],
  data() {
    return {
      currentOpen: null
    };
  },
  methods: {
    toggleSubMenu(key) {
      if (this.currentOpen === key) {
        this.currentOpen = null;
      } else {
        this.currentOpen = key;
      }
    }
  }
};
</script>
<style scoped>
.sidebar-mask {
  position: fixed;
  top: 47px;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  z-index: 99;
}
.left-sidebar {
  position: fixed;
  top: 47px;
  left: 0;
  width: 180px;
  height: calc(100vh - 47px);
  background: #f4f5f7;
  z-index: 100;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
}
.left-sidebar.open {
  transform: translateX(0);
}
.sidebar-content {
  padding: 4px 0;
  margin-top: 4px;
}
.sidebar-item-wrap {
  width: 100%;
}
.sidebar-item {
  position: relative;
  padding: 10px 12px;
  cursor: pointer;
  margin: 2px 8px;
  border-radius: 6px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  font-size: 14px;
  gap: 4px;
}
.menu-icon {
  font-size: 16px;
}
.menu-text {
  flex: 1;
}
.sidebar-item.sub-item {
  padding-left: 44px;
}
.sidebar-item:hover {
  background-color: #e9ebf0;
}
.sidebar-item.active {
  background-color: #dde0e6;
}
.sidebar-item.active::before {
  content: "";
  position: absolute;
  left: 0px;
  top: 8px;
  bottom: 8px;
  width: 5px;
  background-color: #0A2463;
  border-radius: 0 4px 4px 0;
}
/* 箭头：默认朝上，展开朝下 */
.arrow {
  width: 8px;
  height: 8px;
  border: solid #333;
  border-width: 0 2px 2px 0;
  transform: rotate(-135deg);
  transition: transform 0.2s ease;
}
.arrow.open {
  transform: rotate(45deg);
}
.submenu {
  width: 100%;
}
</style>
