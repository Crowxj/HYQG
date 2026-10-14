<template>
  <div class="screen-page">
    <div class="bg-image"></div>
    <ScreenHeader />
    <div class="screen-content">
      <div class="col-left">
        <div class="panel panel-1">区域1</div>
        <div class="panel panel-2">区域2</div>
      </div>
      <div class="col-center">
        <!-- 这里直接引用抽离出来的组件 -->
        <div class="panel panel-3">
          <StatCardGroup :config-list="rawConfig" />
        </div>
        <div class="panel panel-4">区域4</div>
        <div class="panel panel-5">区域5</div>
      </div>
      <div class="col-right">
        <div class="panel panel-6">区域6</div>
        <div class="panel panel-7">区域7</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {} from 'vue'
import ScreenHeader from './ScreenHeader.vue'
// 引入新组件，路径按你实际目录调整
import StatCardGroup from './StatCardGroup.vue'

// 业务配置留在父组件
const config1 = { number: [12498], content: '{nt} 件', label: '区域 A' }
const config2 = { number: [1321], content: '{nt} 件', label: '区域 B' }
const config3 = { number: [33], content: '{nt} 件', label: '区域 C' }
const config4 = { number: [10], content: '{nt} 件', label: '区域 D' }
const rawConfig = [config1, config2, config3, config4]
</script>

<style scoped>
.screen-page {
  width: 100vw;
  height: 100vh;
  position: relative;
  overflow: hidden;
  background-color: #081430;
}
.bg-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  background: url('@/assets/images/screen_bg.webp') no-repeat center center;
  background-size: cover;
  z-index: 0;
}
.screen-content {
  position: relative;
  z-index: 2;
  width: 100%;
  height: calc(100vh - 95px);
  padding: 0 20px 12px 20px;
  margin-top: -40px;
  display: grid;
  grid-template-columns: 0.7fr 1.6fr 0.7fr;
  column-gap: 12px;
}
.col-left,
.col-right {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  height: 100%;
}
.col-center {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  height: 100%;
}
.col-left .panel,
.col-right .panel {
  flex: 1;
}
.panel-3.panel {
  flex: 0.6;
  padding: 8px 12px;
  overflow: visible;
  background: transparent !important;
  border: none !important;
  border-radius: 0 !important;
}
.panel-4 {
  flex: 2.4;
}
.panel-5 {
  flex: 1;
}
.panel {
  background: rgba(20, 50, 110, 0.4);
  border: 1px solid rgba(100, 180, 255, 0.3);
  border-radius: 6px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  overflow: visible;
}
</style>
