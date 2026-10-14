<template>
  <div class="panel panel-5 bottom-offset-20">
    <div class="carousel-table">
      <div class="carousel-header">
        <div class="col">设备</div>
        <div class="col">产量</div>
        <div class="col">状态</div>
        <div class="col">效率</div>
      </div>
      <div class="carousel-body">
        <div class="carousel-track" :style="{ animationDuration: scrollDuration + 's' }">
          <div v-for="(row, index) in tableData" :key="'a' + index" class="carousel-row">
            <div class="col">{{ row.device }}</div>
            <div class="col">{{ row.output }}</div>
            <div class="col status" :class="row.statusClass">{{ row.status }}</div>
            <div class="col">{{ row.efficiency }}</div>
          </div>
          <div v-for="(row, index) in tableData" :key="'b' + index" class="carousel-row">
            <div class="col">{{ row.device }}</div>
            <div class="col">{{ row.output }}</div>
            <div class="col status" :class="row.statusClass">{{ row.status }}</div>
            <div class="col">{{ row.efficiency }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const { tableData, scrollDuration } = defineProps({
  tableData: {
    type: Array,
    required: true,
  },
  scrollDuration: {
    type: Number,
    required: true,
  },
})
</script>

<style scoped>
.panel-5 {
  flex: 1.6;
  padding: 0;
  overflow: hidden;
  background: rgba(10, 30, 60, 0.5);
  border: 1px solid rgba(100, 180, 255, 0.3);
  border-radius: 6px;
  backdrop-filter: blur(2px);
  position: relative;
  min-height: 0;
  height: 0;
}
.carousel-table {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  color: #e0f0ff;
  font-size: 14px;
}
.carousel-header {
  display: flex;
  background: rgba(20, 70, 140, 0.8);
  border-bottom: 1px solid #2e7ad6;
  padding: 8px 10px;
  font-weight: 600;
  color: #b8dcff;
  letter-spacing: 1px;
  text-shadow: 0 0 6px #0077ff;
  flex-shrink: 0;
}
.carousel-header .col {
  flex: 1;
  text-align: left;
  padding-left: 6px;
  border-right: 1px solid rgba(80, 160, 255, 0.3);
}
.carousel-header .col:last-child {
  border-right: none;
}
.carousel-body {
  flex: 1;
  position: relative;
  overflow: hidden;
  padding: 2px 0;
  min-height: 0;
}
.carousel-track {
  display: flex;
  flex-direction: column;
  animation: scrollUp linear infinite;
  will-change: transform;
}
.carousel-row {
  display: flex;
  padding: 8px 10px;
  border-bottom: 1px solid rgba(100, 180, 255, 0.15);
  background: transparent;
  transition: background 0.2s;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
}
.carousel-row:hover {
  background: rgba(50, 130, 240, 0.2);
}
.carousel-row .col {
  flex: 1;
  padding-left: 6px;
  color: #cde4ff;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.carousel-row .col.status {
  color: #6effa0;
}
.carousel-row .col.status.warning {
  color: #ffb86b;
}
.carousel-row .col.status.error {
  color: #ff7a7a;
}
@keyframes scrollUp {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-50%);
  }
}
.bottom-offset-20 {
  margin-bottom: 20px !important;
}
</style>
