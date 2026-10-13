<template>
  <canvas ref="bgCanvas" class="snow-canvas"></canvas>
</template>
<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
const bgCanvas = ref(null)
let ctx = null
let snowList = []
let rafId = null

class Snow {
  constructor() {
    this.reset()
  }
  reset() {
    //雪花大小1~6px，和参考html一致
    this.size = Math.random() * 5 + 1
    this.radius = this.size / 2
    this.x = Math.random() * bgCanvas.value.width
    this.y = -10
    //下落速度，大雪花落得更快
    this.fallSpeed = Math.random() * 2 + 1
    this.swayBase = Math.random() * 2 - 1
  }

  update() {
    this.y += this.fallSpeed
    //正弦实现左右摇摆，和原生示例逻辑完全一致
    this.x += this.swayBase + Math.sin(this.y * 0.01) * 0.8
    //超出底部重置回顶部
    if (this.y > bgCanvas.value.height + 10) {
      this.reset()
    }
  }

  draw() {
    ctx.save()
    ctx.beginPath()
    ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2)
    //浅蓝色雪花，和参考html #84c8ff匹配
    ctx.fillStyle = '#84c8ff'
    ctx.fill()
    ctx.restore()
  }
}

//初始化雪花
function createSnowPool() {
  const count = 60
  const arr = []
  for (let i = 0; i < count; i++) {
    const s = new Snow()
    s.y = Math.random() * bgCanvas.value.height
    arr.push(s)
  }
  return arr
}

function resizeCanvas() {
  if (!bgCanvas.value) return
  bgCanvas.value.width = window.innerWidth
  bgCanvas.value.height = window.innerHeight
  snowList = createSnowPool()
}

function loop() {
  if (!bgCanvas.value || !ctx) return
  ctx.clearRect(0, 0, bgCanvas.value.width, bgCanvas.value.height)
  for (const snow of snowList) {
    snow.update()
    snow.draw()
  }
  rafId = requestAnimationFrame(loop)
}

onMounted(() => {
  ctx = bgCanvas.value.getContext('2d')
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)
  loop()
})

onUnmounted(() => {
  cancelAnimationFrame(rafId)
  window.removeEventListener('resize', resizeCanvas)
})
</script>
<style scoped>
.snow-canvas {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}
</style>
