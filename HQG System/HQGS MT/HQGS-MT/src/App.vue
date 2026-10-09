<template>
  <!-- 路由页面在这里渲染，Login页面就显示在此 -->
  <router-view />
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
html, body {
  width:100%;
  height:100%;
  overflow:hidden;
}
#app {
  width:100%;
  height:100%;
}

@font-face {
  font-family: "iconfont";
  src: url('./assets/icon/iconfont.ttf') format('truetype');
}
.iconfont {
  font-family: "iconfont" !important;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>

