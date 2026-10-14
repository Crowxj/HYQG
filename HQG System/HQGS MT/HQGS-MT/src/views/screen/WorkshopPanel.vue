<template>
  <!-- 移除panel类，不再使用全局.panel的背景、边框样式，只保留布局类 -->
  <div class="workshop-wrap">
    <dv-border-box-1>
      <img class="workshop-img" src="@/assets/images/workshop.png" alt="车间示意图" />
    </dv-border-box-1>
  </div>
</template>

<script setup></script>

<style scoped>
/* 替换原来 .panel-4，只做布局，没有背景和边框 */
.workshop-wrap {
  flex: 1.7;
  display: flex;
  justify-content: center;
  overflow: hidden;
  margin: 0 12px;
  min-height: 0;
  /* 删除 background、border、border-radius，完全去掉外层容器边框 */
}

/* 让边框组件100%占满wrap容器 */
.workshop-wrap :deep(.dv-border-box-1) {
  width: 100%;
  height: 100%;
  padding: 0;
  box-sizing: border-box;
}

/* dataV内部插槽容器，控制图片对齐 */
.workshop-wrap :deep(.border-box-content) {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  overflow: hidden;
  box-sizing: border-box;
  padding: 8px;
}

.workshop-img {
  max-width: 128%;
  max-height: 128%;
  width: auto;
  height: auto;
  object-fit: contain;
  margin-bottom: 0;
  transform: translateY(25px) scale(1);
  transition: transform 0.3s ease;
}

.workshop-wrap:hover .workshop-img {
  transform: translateY(25px) scale(1.03);
}
</style>
