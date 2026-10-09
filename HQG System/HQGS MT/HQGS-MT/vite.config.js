import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  server: {
    port: 8080,
    // 代理配置！重点：这里添加proxy，就不用在request.js写完整后端地址了
    proxy: {
      // 匹配所有 /api 开头的请求
      '/api': {
        target: 'http://127.0.0.1:8081', // 你的springboot后端地址
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, '')
        // 不要rewrite！后端接口本身就是 /api/auth/xxx
      }
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
