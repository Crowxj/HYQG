<template>
  <div class="screen-page">
    <div class="bg-image"></div>
    <ScreenHeader />
    <div class="screen-content">
      <div class="col-left">
        <div class="panel panel-1">区域1</div>
        <div class="panel panel-2 bottom-offset-20">区域2</div>
      </div>
      <div class="col-center">
        <div class="panel panel-3">
          <StatCardGroup :config-list="rawConfig" />
        </div>
        <WorkshopPanel />
        <CarouselDeviceTable :table-data="tableData" :scroll-duration="scrollDuration" />
      </div>
      <div class="col-right">
        <div class="panel panel-6">区域6</div>
        <div class="panel panel-7 bottom-offset-20">区域7</div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import ScreenHeader from './ScreenHeader.vue'
import StatCardGroup from './StatCardGroup.vue'
import CarouselDeviceTable from './CarouselDeviceTable.vue'
import WorkshopPanel from './WorkshopPanel.vue'

const config1 = { number: [12498], content: '{nt} 件', label: '区域 A' }
const config2 = { number: [1321], content: '{nt} 件', label: '区域 B' }
const config3 = { number: [33], content: '{nt} 件', label: '区域 C' }
const config4 = { number: [10], content: '{nt} 件', label: '区域 D' }
const rawConfig = [config1, config2, config3, config4]
const tableData = [
  { device: 'CNC-01', output: '3245 件', status: '正常', statusClass: '', efficiency: '98.2%' },
  {
    device: 'CNC-02',
    output: '2876 件',
    status: '待料',
    statusClass: 'warning',
    efficiency: '76.5%',
  },
  { device: '装配线 A', output: '1250 件', status: '正常', statusClass: '', efficiency: '95.7%' },
  {
    device: '焊接机器人',
    output: '890 件',
    status: '故障',
    statusClass: 'error',
    efficiency: '32.1%',
  },
  { device: '喷涂线', output: '2103 件', status: '正常', statusClass: '', efficiency: '91.4%' },
  { device: '检测站', output: '4082 件', status: '正常', statusClass: '', efficiency: '99.0%' },
]
const scrollDuration = computed(() => {
  return Math.max(10, tableData.length * 2)
})
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
  overflow: hidden;
}
.col-left,
.col-right {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  height: 100%;
  min-height: 0;
}
.col-center {
  display: flex;
  flex-direction: column;
  row-gap: 12px;
  height: 100%;
  min-height: 0;
}
.col-left .panel,
.col-right .panel {
  flex: 1;
  min-height: 0;
}
.panel {
  background: rgba(20, 50, 110, 0.4);
  border: 1px solid rgba(100, 180, 255, 0.3);
  border-radius: 6px;
  color: #fff;
  font-size: 24px;
  overflow: hidden;
  box-sizing: border-box;
}
.panel-3.panel {
  flex: 0.6;
  padding: 8px 12px;
  overflow: visible;
  background: transparent !important;
  border: none !important;
  border-radius: 0 !important;
  margin-bottom: -8px;
  min-height: 0;
}
.bottom-offset-20 {
  margin-bottom: 20px !important;
}
</style>
