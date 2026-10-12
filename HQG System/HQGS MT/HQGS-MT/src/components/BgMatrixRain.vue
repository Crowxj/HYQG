<template>
  <canvas ref="bgCanvas" class="matrix-canvas"></canvas>
</template>
<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
const bgCanvas = ref(null)
let ctx = null
let streams = []
let rafId = null
class Stream {
  constructor(baseX) {
    this.baseX = baseX
    this.reset()
  }
  reset() {
    this.x = this.baseX + (Math.random() - 0.5) * 100
    this.len = 8 + Math.floor(Math.random() * 6)
    this.lineH = 26
    this.y = bgCanvas.value.height + Math.random() * bgCanvas.value.height * 0.5
    this.speed = 2.3 + Math.random() * 0.6
    this.nums = []
    for (let i = 0; i < this.len; i++) {
      this.nums.push(Math.random() > 0.5 ? '1' : '0')
    }
  }
  update() {
    this.y -= this.speed
    // 01慢速变换
    for (let i = 0; i < this.nums.length; i++) {
      if (Math.random() < 0.12) {
        this.nums[i] = this.nums[i] === '0' ? '1' : '0'
      }
    }
    if (this.y < -this.len * this.lineH) {
      this.reset()
    }
  }
  draw() {
    ctx.save()
    ctx.shadowColor = '#40a8ff'
    ctx.shadowBlur = 4
    ctx.textAlign = 'center'
    let cy = this.y
    for (let i = 0; i < this.nums.length; i++) {
      const ratio = i / (this.nums.length - 1 || 1)
      // 缩小字号差值，减少尾部变细观感：头部26px，尾部18px
      const fontSize = Math.round(26 - ratio * 8)
      ctx.font = `${fontSize}px monospace`
      // 尾部更暗：衰减0.85，尾部几乎半隐，贴合参考图暗尾巴效果
      const alpha = 1 - ratio * 0.85
      ctx.fillStyle = `rgba(115, 208, 255, ${alpha})`
      ctx.fillText(this.nums[i], this.x, cy)
      cy += this.lineH
    }
    ctx.restore()
  }
}
function createStreams() {
  const count = 5
  const seg = bgCanvas.value.width / count
  const arr = []
  for (let i = 0; i < count; i++) {
    arr.push(new Stream(seg * (i + 0.5)))
  }
  return arr
}
function resizeCanvas() {
  if (!bgCanvas.value) return
  bgCanvas.value.width = window.innerWidth
  bgCanvas.value.height = window.innerHeight
  streams = createStreams()
}
function loop() {
  if (!bgCanvas.value || !ctx) return
  ctx.clearRect(0, 0, bgCanvas.value.width, bgCanvas.value.height)
  for (const s of streams) {
    s.update()
    s.draw()
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
.matrix-canvas {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
}
</style>
