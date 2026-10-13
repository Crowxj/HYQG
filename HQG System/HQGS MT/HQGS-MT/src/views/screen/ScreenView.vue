<template>
  <div class="screen-page">
    <!-- 底层星球星空背景 -->
    <div class="bg-image"></div>
    <!-- 大屏头部 -->
    <div class="page-header">
      <div class="title-row">
        <div class="title-wrap">
          <h1 class="main-title">擎工可视化大屏</h1>
          <!-- 文字正下方：完整左右阶梯+中间错位短线，单个svg居中 -->
          <svg class="title-bottom-svg" width="800" height="60" viewBox="0 0 800 60">
            <!-- 上层深蓝色主线条 整体上移6px -->
            <path
              d="M0 6 L140 6 L160 24 L210 24 L230 36 L400 36 L570 36 L590 24 L640 24 L660 6 L800 6"
              stroke="#0088ff"
              stroke-width="2.5"
              fill="none"
            />
            <!-- 中间下层缩短横线 -->
            <path d="M320 44 L480 44" stroke="#0088ff" stroke-width="2.5" fill="none" />
            <!-- 下层浅青色偏移线条 整体上移6px -->
            <path
              d="M0 12 L140 12 L160 30 L210 30 L230 42 L400 42 L570 42 L590 30 L640 30 L660 12 L800 12"
              stroke="#00e5ff"
              stroke-width="1.8"
              fill="none"
            />
            <!-- 中间下层缩短横线 -->
            <path d="M320 50 L480 50" stroke="#00e5ff" stroke-width="1.8" fill="none" />
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
// 无时间相关代码
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
.page-header {
  width: 100%;
  position: relative;
  z-index: 1;
}
.title-row {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 20px 0px;
}
.title-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.main-title {
  color: #ffffff;
  font-size: 40px;
  letter-spacing: 8px;
  margin: 0 0 -30px 0;
  filter: drop-shadow(0 0 8px #0088ff);
  white-space: nowrap;
}
.title-bottom-svg {
  filter: drop-shadow(0 0 6px #0088ff);
  flex-shrink: 0;
}
</style>
