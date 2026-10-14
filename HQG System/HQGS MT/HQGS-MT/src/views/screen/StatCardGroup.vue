<template>
  <div class="stat-card-group">
    <dv-border-box-8
      v-for="(item, idx) in statList"
      :key="idx"
      :ref="collectBoxRef"
      class="stat-wrap"
      :class="{ 'box-hover': hoverIndex === idx }"
      @mouseenter="handleMouseEnter(idx)"
      @mouseleave="handleMouseLeave"
      :color="['#00a0ff', '#0055bb']"
      :title="String(item.realNum)"
    >
      <div class="stat-card" :class="`loop‑${idx + 1}`">
        <!-- 四个黄色角 -->
        <div class="corner-tl"></div>
        <div class="corner-tr"></div>
        <div class="corner-bl"></div>
        <div class="corner-br"></div>
        <!-- 内层跑马+文字 -->
        <div class="card-inner">
          <div class="run-top"></div>
          <div class="run-right"></div>
          <div class="run-bottom"></div>
          <div class="run-left"></div>
          <div class="stat-num" v-html="item.showText"></div>
          <div class="stat-label">{{ item.label }}</div>
        </div>
      </div>
    </dv-border-box-8>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'

const props = defineProps({
  configList: {
    type: Array,
    required: true,
    /**
     * 每项结构：{ number:[xxx], content:'{nt} 件', label:'xxx' }
     */
  },
})

// 计算处理：>=99展示99上标+
const statList = computed(() => {
  return props.configList.map((cfg) => {
    const realNum = cfg.number[0]
    let showNumText
    if (realNum >= 99) {
      showNumText = '99<sup>+</sup>'
    } else {
      showNumText = realNum.toLocaleString()
    }
    const showText = cfg.content.replace('{nt}', showNumText)
    return {
      realNum,
      showText,
      label: cfg.label,
    }
  })
})

const boxRefList = ref([])
const hoverIndex = ref(-1)

const collectBoxRef = (el) => {
  if (el) boxRefList.value.push(el)
}
const handleMouseEnter = (idx) => {
  hoverIndex.value = idx
}
const handleMouseLeave = () => {
  hoverIndex.value = -1
}

onMounted(async () => {
  await nextTick()
  setTimeout(() => {
    boxRefList.value.forEach((comp) => {
      if (comp && typeof comp.initWH === 'function') {
        comp.initWH()
      }
    })
  }, 200)
})
</script>

<style scoped>
.stat-card-group {
  width: 100%;
  height: 100%;
  display: flex;
  gap: 12px;
}
.stat-wrap {
  flex: 1;
  height: 100%;
  overflow: visible !important;
  transition:
    transform 0.24s ease,
    box-shadow 0.24s ease;
}
.stat-wrap.box-hover {
  transform: translateY(-6px);
  box-shadow: 0 8px 20px rgba(0, 160, 255, 0.25);
}
.stat-card {
  width: 100%;
  height: 100%;
  position: relative;
  box-sizing: border-box;
  border-radius: 4px;
  --corner-size: 6px;
  --corner-offset: 2px;
  --speed: 6s;
  cursor: pointer;
}
.card-inner {
  position: relative;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  padding: 4px 4px 12px 4px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(12, 35, 78, 0.55);
  border-radius: 4px;
  overflow: hidden;
}
/* 四角黄色三角 */
.corner-tl {
  position: absolute;
  top: var(--corner-offset);
  left: var(--corner-offset);
  width: 0;
  height: 0;
  border-top: var(--corner-size) solid #f9e623;
  border-right: var(--corner-size) solid transparent;
  z-index: 15;
}
.corner-tr {
  position: absolute;
  top: var(--corner-offset);
  right: var(--corner-offset);
  width: 0;
  height: 0;
  border-top: var(--corner-size) solid #f9e623;
  border-left: var(--corner-size) solid transparent;
  z-index: 15;
}
.corner-bl {
  position: absolute;
  bottom: var(--corner-offset);
  left: var(--corner-offset);
  width: 0;
  height: 0;
  border-bottom: var(--corner-size) solid #f9e623;
  border-right: var(--corner-size) solid transparent;
  z-index: 15;
}
.corner-br {
  position: absolute;
  bottom: var(--corner-offset);
  right: var(--corner-offset);
  width: 0;
  height: 0;
  border-bottom: var(--corner-size) solid #f9e623;
  border-left: var(--corner-size) solid transparent;
  z-index: 15;
}
/* 跑马流光 */
.run-top {
  position: absolute;
  top: 0;
  left: -120px;
  width: 120px;
  height: 2px;
  background: linear-gradient(90deg, rgba(0, 180, 255, 0), #00d0ff, rgba(0, 180, 255, 0));
  z-index: 6;
}
.run-right {
  position: absolute;
  right: 0;
  top: -120px;
  width: 2px;
  height: 120px;
  background: linear-gradient(180deg, rgba(0, 180, 255, 0), #00d0ff, rgba(0, 180, 255, 0));
  z-index: 6;
}
.run-bottom {
  position: absolute;
  bottom: 0;
  right: -120px;
  width: 120px;
  height: 2px;
  background: linear-gradient(270deg, rgba(0, 180, 255, 0), #00d0ff, rgba(0, 180, 255, 0));
  z-index: 6;
}
.run-left {
  position: absolute;
  left: 0;
  bottom: -120px;
  width: 2px;
  height: 120px;
  background: linear-gradient(0deg, rgba(0, 180, 255, 0), #00d0ff, rgba(0, 180, 255, 0));
  z-index: 6;
}
.stat-num,
.stat-label {
  z-index: 10;
}
.loop‑1 .run-top {
  animation: topRun var(--speed) linear infinite;
}
.loop‑1 .run-right {
  animation: rightRun var(--speed) linear infinite;
}
.loop‑1 .run-bottom {
  animation: bottomRun var(--speed) linear infinite;
}
.loop‑1 .run-left {
  animation: leftRun var(--speed) linear infinite;
}
.loop‑2 .run-top {
  animation: topRun var(--speed) linear infinite;
  animation-delay: -1.5s;
}
.loop‑2 .run-right {
  animation: rightRun var(--speed) linear infinite;
  animation-delay: -1.5s;
}
.loop‑2 .run-bottom {
  animation: bottomRun var(--speed) linear infinite;
  animation-delay: -1.5s;
}
.loop‑2 .run-left {
  animation: leftRun var(--speed) linear infinite;
  animation-delay: -1.5s;
}
.loop‑3 .run-top {
  animation: topRun var(--speed) linear infinite;
  animation-delay: -3s;
}
.loop‑3 .run-right {
  animation: rightRun var(--speed) linear infinite;
  animation-delay: -3s;
}
.loop‑3 .run-bottom {
  animation: bottomRun var(--speed) linear infinite;
  animation-delay: -3s;
}
.loop‑3 .run-left {
  animation: leftRun var(--speed) linear infinite;
  animation-delay: -3s;
}
.loop‑4 .run-top {
  animation: topRun var(--speed) linear infinite;
  animation-delay: -4.5s;
}
.loop‑4 .run-right {
  animation: rightRun var(--speed) linear infinite;
  animation-delay: -4.5s;
}
.loop‑4 .run-bottom {
  animation: bottomRun var(--speed) linear infinite;
  animation-delay: -4.5s;
}
.loop‑4 .run-left {
  animation: leftRun var(--speed) linear infinite;
  animation-delay: -4.5s;
}

@keyframes topRun {
  0% {
    left: -120px;
  }
  25% {
    left: 100%;
  }
  25.01% {
    left: 100%;
  }
  100% {
    left: 100%;
  }
}
@keyframes rightRun {
  0%,
  25% {
    top: -120px;
  }
  50% {
    top: 100%;
  }
  50.01% {
    top: 100%;
  }
  100% {
    top: 100%;
  }
}
@keyframes bottomRun {
  0%,
  50% {
    right: -120px;
  }
  75% {
    right: 100%;
  }
  75.01% {
    right: 100%;
  }
  100% {
    right: 100%;
  }
}
@keyframes leftRun {
  0%,
  75% {
    bottom: -120px;
  }
  100% {
    bottom: 100%;
  }
}

.stat-num {
  font-size: 28px;
  color: #ffffff;
  font-weight: 700;
  margin-bottom: 6px;
  font-family: 'Microsoft YaHei', sans-serif;
  text-align: center;
  white-space: nowrap;
}
.stat-num :deep(sup) {
  font-size: 0.55em;
  vertical-align: super;
  line-height: 1;
}
.stat-label {
  font-size: 18px;
  color: #8fd1ff;
  font-family: 'Microsoft YaHei', sans-serif;
  text-align: center;
  white-space: nowrap;
}
</style>
