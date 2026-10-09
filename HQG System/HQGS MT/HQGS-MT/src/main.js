import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
// 引入DataV
import DataVVue3 from '@kjgl77/datav-vue3'
import './assets/icon/iconfont.ttf'
//element‑plus
// import 'element-plus/dist/index.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(DataVVue3) // 全局注册DataV

app.mount('#app')
