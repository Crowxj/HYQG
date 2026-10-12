<template>
  <div class="login-container">
    <!-- 底层星空背景 -->
    <div class="bg-image"></div>
    <!-- 中间层：抽离出来的数字雨组件 -->
    <BgMatrixRain />

    <div class="logo-box">
      <img src="@/assets/images/login_logo.webp" alt="浩研汇成LOGO" />
    </div>
    <div class="box-wrap">
      <dv-border-box-8 class="login-panel">
        <div class="panel-inner">
          <h1 class="system-title">擎工可视化系统</h1>
          <dv-decoration-5 style="width: 400px; height: 40px" class="title-decoration" />
          <div class="tab-text">账号登录</div>
          <!--账号输入-->
          <div class="input-item">
            <div class="input-wrap">
              <i class="my-icon input-icon">&#xe63f;</i>
              <input
                class="input-box account-input"
                placeholder="请输入账号"
                v-model="account"
                @blur="validateAccount"
                @keyup.enter="handleLogin"
              />
              <span class="input-suffix">@kyhorizon.com</span>
            </div>
          </div>
          <!--密码输入-->
          <div class="input-item">
            <div class="input-wrap">
              <i class="my-icon input-icon">&#xe654;</i>
              <input
                class="input-box pwd-input"
                placeholder="请输入密码"
                v-model="password"
                :type="pwdVisible ? 'text' : 'password'"
                @keyup.enter="handleLogin"
              />
              <div class="pwd-icon-group">
                <i v-if="password" class="my-icon input-clear" @click="password = ''">&#xe602;</i>
                <i class="my-icon input-eye" @click="pwdVisible = !pwdVisible">
                  {{ pwdVisible ? '&#xe63d;' : '&#xe653;' }}
                </i>
              </div>
            </div>
          </div>
          <div class="row-remember">
            <label class="checkbox-wrap">
              <input type="checkbox" v-model="remember" />
              <span>记住登录状态</span>
            </label>
          </div>
          <div class="msg-line">
            <span v-if="accountError" class="error-text">{{ accountError }}</span>
          </div>
          <button class="login-btn" @click="handleLogin">登 录</button>
          <div class="row-link">
            <span class="link-item" @click="goRegister">注册账号</span>
            <span class="split-line">|</span>
            <span class="link-item" @click="goForgetPwd">忘记密码</span>
          </div>
          <div class="footer-text">版权所有 © 2025 KY‑Horizon 自动化技术（天津）有限公司</div>
        </div>
      </dv-border-box-8>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'
// 引入抽离的数字雨组件
import BgMatrixRain from '@/components/BgMatrixRain.vue'

const router = useRouter()
const account = ref('')
const password = ref('')
const remember = ref(false)
const pwdVisible = ref(false)
const accountError = ref('')
const emailSuffix = '@kyhorizon.com'
const accountReg = /^[a-zA-Z][a-zA-Z0-9_]{2,17}$/

// 读取本地记住账号
onMounted(() => {
  const savedAccount = localStorage.getItem('savedAccount')
  const savedRemember = localStorage.getItem('rememberFlag')
  if (savedRemember === 'true' && savedAccount) {
    account.value = savedAccount
    remember.value = true
  }
})

// 自动清除@后缀，清空错误提示
watch(account, (val) => {
  if (val.includes('@')) account.value = val.split('@')[0]
  accountError.value = ''
})

// 账号校验
const validateAccount = () => {
  const val = account.value.trim()
  if (!val) {
    accountError.value = ''
    return false
  }
  if (!accountReg.test(val)) {
    accountError.value = '账号格式错误：3~18字符，字母开头，仅字母数字下划线'
    return false
  }
  accountError.value = ''
  return true
}

// 登录核心逻辑
const handleLogin = async () => {
  if (!validateAccount()) return
  const fullEmail = account.value.trim() + emailSuffix
  const pwd = password.value.trim()
  if (!pwd) {
    accountError.value = '请输入密码'
    return
  }
  try {
    const res = await login({ username: fullEmail, password: pwd })
    if (res.data.code !== 200) {
      accountError.value = res.data.message || '登录失败'
      return
    }
    // 修复token[object Object]问题，取出字符串
    const jwtToken = res.data.data.token
    localStorage.setItem('token', jwtToken)
    localStorage.setItem('username', fullEmail)
    if (remember.value) {
      localStorage.setItem('savedAccount', account.value.trim())
      localStorage.setItem('rememberFlag', 'true')
    } else {
      localStorage.removeItem('savedAccount')
      localStorage.removeItem('rememberFlag')
    }
    router.push('/dashboard')
  } catch (err) {
    accountError.value = err?.response?.data?.message || err?.message || '请求异常'
  }
}
const goRegister = () => router.push('/dashboard/register')
const goForgetPwd = () => router.push('/dashboard/forgetPwd')
</script>

<style scoped>
@font-face {
  font-family: 'my-icon';
  src: url('@/assets/icon/iconfont.ttf') format('truetype');
}
.my-icon {
  font-family: 'my-icon' !important;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
}
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  position: relative;
  overflow: hidden;
}
/*最底层：星空背景图片*/
.bg-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  background: url('@/assets/images/screen_bg.webp') no-repeat center center;
  background-size: cover;
  z-index: 0;
}

.logo-box {
  position: absolute;
  top: 24px;
  left: 24px;
  z-index: 99;
}
.logo-box img {
  height: 46px;
  width: auto;
  object-fit: contain;
  filter: drop-shadow(0 0 6px rgba(80, 180, 255, 0.4));
}
.box-wrap {
  position: relative;
  z-index: 10;
  width: 520px;
  height: 680px;
  border-radius: 12px;
  overflow: hidden;
}
.login-panel {
  width: 100%;
  height: 100%;
  background-color: rgba(8, 26, 56, 0.65);
}
.panel-inner {
  padding: 32px 28px;
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
}
.system-title {
  font-size: 34px;
  color: #ffffff;
  text-align: center;
  margin: 0 0 8px;
  letter-spacing: 2px;
}
.title-decoration {
  align-self: center;
  margin-bottom: 24px;
}
.tab-text {
  font-size: 26px;
  text-align: center;
  margin-bottom: 40px;
  background: linear-gradient(90deg, #7fc8ff, #ffffff);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  letter-spacing: 4px;
  filter: drop-shadow(0 0 6px #3374cc);
}
.input-item {
  margin-bottom: 20px;
}
.input-wrap {
  position: relative;
  width: 100%;
}
.input-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 20px;
  color: #92bfff;
  z-index: 2;
}
.input-suffix {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  color: #92bfff;
  z-index: 2;
  pointer-events: none;
}
.pwd-icon-group {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 2;
  display: flex;
  align-items: center;
  gap: 10px;
}
.input-clear {
  font-size: 15px;
  color: #92bfff;
  cursor: pointer;
}
.input-eye {
  font-size: 20px;
  color: #92bfff;
  cursor: pointer;
}
.input-box {
  width: 100%;
  height: 44px;
  box-sizing: border-box;
  background: rgba(20, 50, 90, 0.45);
  border: 1px solid #3374cc;
  border-radius: 10px;
  color: #fff;
  padding: 0 14px 0 48px;
  font-size: 16px;
  outline: none;
  transition: border-color 0.2s;
}
.account-input {
  padding-right: 140px;
}
.pwd-input {
  padding-right: 70px;
}
.pwd-input::-ms-reveal,
.pwd-input::-webkit-contacts-auto-fill-button,
.pwd-input::-webkit-credentials-auto-fill-button {
  visibility: hidden;
  display: none !important;
  pointer-events: none;
  width: 0;
  height: 0;
}
.input-box:focus {
  border-color: #46b8bb;
}
.input-box::placeholder {
  color: #92bfff;
}
.row-remember {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-bottom: 8px;
  color: #c0ddff;
}
.checkbox-wrap {
  display: flex;
  align-items: center;
  cursor: pointer;
}
.checkbox-wrap input {
  margin: 0;
  margin-right: 6px;
}
.msg-line {
  margin-bottom: 32px;
  font-size: 12px;
  line-height: 1.4;
  min-height: 18px;
}
.error-text {
  color: #ff7875;
}
.login-btn {
  width: 100%;
  height: 48px;
  font-size: 20px;
  background: linear-gradient(90deg, #0c57bb, #1580e8);
  border: none;
  color: #fff;
  border-radius: 10px;
  box-shadow: 0 0 12px #1580e888;
  cursor: pointer;
  transition: all 0.2s ease;
}
.login-btn:hover {
  background: linear-gradient(90deg, #094896, #106bc2);
  box-shadow: 0 0 14px #1580e890;
}
.login-btn:active {
  background: linear-gradient(90deg, #073b7c, #0c57bb);
  transform: scale(0.98);
}
.row-link {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 16px 0 24px;
  font-size: 14px;
  gap: 12px;
}
.split-line {
  color: #69b8ff;
}
.link-item {
  color: #69b8ff;
  cursor: pointer;
  transition: color 0.2s;
}
.link-item:hover {
  color: #ffffff;
}
.footer-text {
  margin-top: auto;
  text-align: center;
  color: #93bfff;
  font-size: 12px;
}
</style>
