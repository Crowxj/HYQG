<template>
  <div class="screen-page">
    <!-- 底层星球星空背景 -->
    <div class="bg-image"></div>
    <!-- 大屏头部 -->
    <div class="page-header">
      <div class="title-row">
        <!-- 左侧Logo：独立悬浮层，不影响标题居中 -->
        <div class="header-logo">
          <img src="@/assets/images/login_logo.webp" alt="浩研汇成logo" />
        </div>
        <!-- 中间标题+装饰线：保持原有居中样式不变 -->
        <div class="title-wrap">
          <h1 class="main-title">擎工可视化大屏</h1>
          <svg class="title-bottom-svg" viewBox="0 0 1920 110" preserveAspectRatio="xMidYMid meet">
            <defs>
              <!-- 主线流光：中间向两侧扩散 -->
              <mask id="maskBlue">
                <rect x="960" y="0" width="160" height="110" fill="#fff">
                  <animate
                    attributeName="x"
                    values="960;960;1920"
                    keyTimes="0;0.02;1"
                    dur="5s"
                    repeatCount="indefinite"
                  />
                </rect>
                <rect x="960" y="0" width="160" height="110" fill="#fff">
                  <animate
                    attributeName="x"
                    values="960;960;-160"
                    keyTimes="0;0.02;1"
                    dur="5s"
                    repeatCount="indefinite"
                  />
                </rect>
              </mask>
              <mask id="maskCyan">
                <rect x="960" y="0" width="130" height="110" fill="#fff">
                  <animate
                    attributeName="x"
                    values="960;960;1920"
                    keyTimes="0;0.02;1"
                    dur="4.2s"
                    repeatCount="indefinite"
                  />
                </rect>
                <rect x="960" y="0" width="130" height="110" fill="#fff">
                  <animate
                    attributeName="x"
                    values="960;960;-130"
                    keyTimes="0;0.02;1"
                    dur="4.2s"
                    repeatCount="indefinite"
                  />
                </rect>
              </mask>
            </defs>
            <!-- 上层深蓝主线：【删掉左侧水平段】起点直接在折线 300,8 -->
            <path
              d="
M300 8
L380 24
L640 24
L720 48
L960 48
L1200 48
L1280 24
L1540 24
L1620 8
    "
              stroke="#0088ff"
              stroke-width="2.5"
              fill="none"
            />
            <!-- 上层流光高亮层 -->
            <path
              d="
M300 8
L380 24
L640 24
L720 48
L960 48
L1200 48
L1280 24
L1540 24
L1620 8
    "
              stroke="#42d2db"
              stroke-width="3.4"
              fill="none"
              mask="url(#maskBlue)"
              filter="drop-shadow(0 0 9px #24b0bc)"
            />
            <!-- 下层浅青线 -->
            <path
              d="
M300 16
L380 32
L640 32
L720 56
L960 56
L1200 56
L1280 32
L1540 32
L1620 16
    "
              stroke="#00e5ff"
              stroke-width="1.8"
              fill="none"
            />
            <!-- 下层流光高亮层 -->
            <path
              d="
M300 16
L380 32
L640 32
L720 56
L960 56
L1200 56
L1280 32
L1540 32
L1620 16
    "
              stroke="#60e2eb"
              stroke-width="2.6"
              fill="none"
              mask="url(#maskCyan)"
              filter="drop-shadow(0 0 10px #36c8d4)"
            />
            <!-- 左侧闪烁方块 保持不变 -->
            <g
              fill="none"
              stroke="#84ffff"
              stroke-width="2.8"
              filter="drop-shadow(0 0 7px #00e5ff)"
            >
              <polygon points="645,42 650,42 695,55 690,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.06;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="625,42 630,42 675,55 670,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.185;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="605,42 610,42 655,55 650,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.31;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="585,42 590,42 635,55 630,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.435;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="565,42 570,42 615,55 610,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.56;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="545,42 550,42 595,55 590,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.685;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="525,42 530,42 575,55 570,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.81;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="505,42 510,42 555,55 550,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.935;0.96;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
            </g>
            <!-- 右侧闪烁方块 保持不变 -->
            <g
              fill="none"
              stroke="#84ffff"
              stroke-width="2.8"
              filter="drop-shadow(0 0 7px #00e5ff)"
            >
              <polygon points="1270,42 1275,42 1235,55 1230,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.06;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="1290,42 1295,42 1255,55 1250,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.185;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="1310,42 1315,42 1275,55 1270,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.31;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="1330,42 1335,42 1295,55 1290,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.435;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="1350,42 1355,42 1315,55 1310,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.56;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="1370,42 1375,42 1335,55 1330,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.685;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="1390,42 1395,42 1355,55 1350,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.81;0.85;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
              <polygon points="1410,42 1415,42 1375,55 1370,55" opacity="0">
                <animate
                  attributeName="opacity"
                  values="0;1;1;0"
                  keyTimes="0;0.935;0.96;1"
                  dur="4s"
                  begin="0s"
                  repeatCount="indefinite"
                />
              </polygon>
            </g>
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
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px 20px 0px;
}
.header-logo {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 2;
}
.header-logo img {
  height: 46px;
  object-fit: contain;
  display: block;
}
.title-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}
.main-title {
  color: #ffffff;
  font-size: 40px;
  letter-spacing: 8px;
  margin: 0 0 -45px 0;
  filter: drop-shadow(0 0 8px #0088ff);
  white-space: nowrap;
}
.title-bottom-svg {
  width: 100%;
  height: 110px;
  filter: drop-shadow(0 0 6px #0088ff);
  flex-shrink: 0;
}
</style>
