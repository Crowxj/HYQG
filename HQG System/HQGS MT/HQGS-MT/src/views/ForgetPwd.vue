<template>
  <dv-full-screen-container fit="fill">
    <div class="login-container">
      <!-- 左上角Logo -->
      <div class="logo-box">
        <img src="@/assets/images/login_logo.webp" alt="浩研汇成LOGO" />
      </div>
      <div class="box-wrap">
        <dv-border-box-8 class="login-panel">
          <div class="panel-inner">
            <h1 class="system-title">擎工可视化系统</h1>
            <!-- 装饰组件放到标题下方 -->
            <dv-decoration-5 style="width:400px;height:40px;" class="title-decoration" />
            <!-- 找回密码标题 -->
            <div class="tab-text">找回密码</div>
            <!-- 账号输入框（带用户图标 + 右侧固定邮箱后缀@kyhorizon.com） -->
            <div class="input-item">
              <div class="input-wrap">
                <i class="my-icon input-icon">&#xe63f;</i>
                <input
                  class="input-box account-input"
                  placeholder="请输入账号"
                  v-model="account"
                  @focus="showAccountTip = true"
                  @blur="handleAccountBlur"
                />
                <span class="input-suffix">@kyhorizon.com</span>
              </div>
              <!-- 【合并为单行提示】二选一展示，不会两行同时出现 -->
              <div class="msg-line">
                <span v-if="accountError" class="error-text">{{ accountError }}</span>
                <span v-else-if="showAccountTip" class="tip-text">6~18个字符，可使用字母、数字、下划线，需要以字母开头</span>
              </div>
            </div>
            <!-- 邮箱验证码输入框 -->
            <div class="input-item">
              <div class="input-wrap code-wrap">
                <i class="my-icon input-icon">&#xe699;</i>
                <input
                  class="input-box code-input"
                  placeholder="请输入邮箱验证码"
                  v-model="code"
                />
                <button
                  class="get-code-btn"
                  @click="getCode"
                  :disabled="countDown > 0"
                >
                  {{ countDown > 0 ? `${countDown}s后重发` : '获取验证码' }}
                </button>
              </div>
            </div>
            <!-- 新密码输入框 -->
            <div class="input-item">
              <div class="input-wrap">
                <i class="my-icon input-icon">&#xe654;</i>
                <input
                  class="input-box pwd-input"
                  placeholder="请输入新密码"
                  v-model="newPwd"
                  :type="pwdVisible ? 'text' : 'password'"
                />
                <div class="pwd-icon-group">
                  <i v-if="newPwd" class="my-icon input-clear" @click="newPwd=''">&#xe602;</i>
                  <i v-if="pwdVisible" class="my-icon input-eye" @click="pwdVisible = !pwdVisible">&#xe63d;</i>
                  <i v-else class="my-icon input-eye" @click="pwdVisible = !pwdVisible">&#xe653;</i>
                </div>
              </div>
            </div>
            <!-- 确认新密码 -->
            <div class="input-item">
              <div class="input-wrap">
                <i class="my-icon input-icon">&#xe654;</i>
                <input
                  class="input-box pwd-input"
                  placeholder="请再次输入新密码"
                  v-model="confirmPwd"
                  :type="confirmPwdVisible ? 'text' : 'password'"
                />
                <div class="pwd-icon-group">
                  <i v-if="confirmPwd" class="my-icon input-clear" @click="confirmPwd=''">&#xe602;</i>
                  <i v-if="confirmPwdVisible" class="my-icon input-eye" @click="confirmPwdVisible = !confirmPwdVisible">&#xe63d;</i>
                  <i v-else class="my-icon input-eye" @click="confirmPwdVisible = !confirmPwdVisible">&#xe653;</i>
                </div>
              </div>
            </div>
            <button class="login-btn" @click="handleReset">确认重置</button>
            <!-- 返回登录链接【增加箭头图标，和注册页样式统一】 -->
            <div class="row-link">
              <span class="link-item" @click="goLogin">返回登录 <i class="my-icon back-arrow">&#xe61d;</i></span>
            </div>
            <div class="footer-text">版权所有 © 2025 KY-Horizon 自动化技术（天津）有限公司</div>
          </div>
        </dv-border-box-8>
      </div>
    </div>
  </dv-full-screen-container>
</template>
<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
const account = ref('')
const code = ref('')
const newPwd = ref('')
const confirmPwd = ref('')
const pwdVisible = ref(false)
const confirmPwdVisible = ref(false)
const countDown = ref(0)
const emailSuffix = '@kyhorizon.com'
// 账号提示、错误信息
const showAccountTip = ref(false)
const accountError = ref('')
// 账号正则：字母开头，6~18位，只能字母数字下划线
const accountReg = /^[a-zA-Z][a-zA-Z0-9_]{5,17}$/
watch(account, (val) => {
  if (val.includes('@')) {
    account.value = val.split('@')[0]
  }
  // 输入变化清空错误
  accountError.value = ''
})
// 账号失去焦点：隐藏提示文字，执行校验
const handleAccountBlur = () => {
  showAccountTip.value = false
  validateAccount()
}
// 账号校验函数
const validateAccount = () => {
  const val = account.value.trim()
  if (!val) {
    accountError.value = ''
    return false
  }
  if (!accountReg.test(val)) {
    accountError.value = '账号格式错误：6~18字符，字母开头，仅字母数字下划线'
    return false
  }
  accountError.value = ''
  return true
}
// 获取验证码
const getCode = () => {
  const val = account.value.trim()
  if (!val) {
    alert('请输入账号')
    return
  }
  if (!validateAccount()) return
  const fullEmail = val + emailSuffix
  console.log('发送重置密码验证码到邮箱：', fullEmail)
  // 后端接口
  /*
  api.sendResetPwdCode({ email: fullEmail }).then(()=>{
    alert('验证码已发送，请查收邮箱')
  })
  */
  countDown.value = 60
  const timer = setInterval(() => {
    countDown.value--
    if (countDown.value <= 0) clearInterval(timer)
  }, 1000)
}
// 重置密码提交
const handleReset = () => {
  const val = account.value.trim()
  if (!val) {
    alert('请输入账号')
    return
  }
  if (!validateAccount()) return
  if (!code.value) {
    alert('请输入验证码')
    return
  }
  if (!newPwd.value) {
    alert('请输入新密码')
    return
  }
  if (newPwd.value !== confirmPwd.value) {
    alert('两次密码输入不一致')
    return
  }
  const fullEmail = val + emailSuffix
  console.log('重置密码', fullEmail, newPwd.value, code.value)
  /*
  api.resetPassword({
    email: fullEmail,
    code: code.value,
    newPassword: newPwd.value
  }).then(res=>{
    alert('密码重置成功，请重新登录')
    router.push('/login')
  })
  */
}
// 返回登录页
const goLogin = () => {
  router.push('/login')
}
</script>
<style scoped>
@font-face {
  font-family: "my-icon";
  src: url('@/assets/icon/iconfont.ttf') format('truetype');
}
.my-icon {
  font-family: "my-icon" !important;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
}
.login-container {
  width: 100%;
  height: 100%;
  background: url('@/assets/images/login-bg.webp') no-repeat center center;
  background-size: cover;
  display: flex;
  align-items: flex-start;
  justify-content: flex-end;
  padding-right: 10%;
  padding-top: 8%;
  box-sizing: border-box;
  position: relative;
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
/* 和登录、注册页面完全一致的渐变发光标题 */
.tab-text {
  font-size: 26px;
  text-align: center;
  margin-bottom: 40px;
  /* 渐变文字 */
  background: linear-gradient(90deg, #7fc8ff, #ffffff);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  letter-spacing: 4px;
  /* 发光效果 */
  filter: drop-shadow(0 0 6px #3374cc);
}
.input-item {
  margin-bottom: 20px;
}
.input-wrap {
  position: relative;
  width:100%;
}
/* ==========单行提示样式========== */
.msg-line {
  margin-top:4px;
  font-size:12px;
  line-height:1.4;
}
.tip-text {
  color: #92bfff;
}
.error-text {
  color: #ff7875;
}
.code-wrap {
  display: flex;
  gap:10px;
}
.code-input {
  flex:1;
  padding-right: 10px !important;
}
.get-code-btn {
  width: 130px;
  height:44px;
  flex-shrink:0;
  font-size:14px;
  background: linear-gradient(90deg, #0c57bb, #1580e8);
  border: none;
  color:#fff;
  border-radius:10px;
  cursor:pointer;
  box-shadow: 0 0 8px #1580e866;
  transition: all 0.2s ease;
}
/*悬停渐变加深*/
.get-code-btn:hover:not(:disabled) {
  background: linear-gradient(90deg, #094896, #106bc2);
  box-shadow: 0 0 10px #1580e880;
}
/*点击按下*/
.get-code-btn:active:not(:disabled) {
  background: linear-gradient(90deg, #073b7c, #0c57bb);
  transform: scale(0.98);
}
.get-code-btn:disabled {
  background:#445880;
  cursor:not-allowed;
  box-shadow:none;
  transform:none;
}
.input-icon {
  position: absolute;
  left:16px;
  top: 50%;
  transform: translateY(-50%);
  font-size:20px;
  color:#92bfff;
  z-index:2;
}
.input-suffix {
  position: absolute;
  right:16px;
  top: 50%;
  transform: translateY(-50%);
  font-size:16px;
  color:#92bfff;
  z-index:2;
  pointer-events: none;
}
.pwd-icon-group {
  position: absolute;
  right: 16px;
  top: 50%;
  transform: translateY(-50%);
  z-index:2;
  display: flex;
  align-items: center;
  gap: 10px;
}
.input-clear {
  font-size:15px;
  color:#92bfff;
  cursor: pointer;
}
.input-eye {
  font-size:20px;
  color:#92bfff;
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
.account-input{
  padding-right: 140px;
}
.pwd-input{
  padding-right: 70px;
  -webkit-appearance: none;
}
/* 清除浏览器原生密码眼睛 */
.pwd-input::-ms-reveal {
  display: none !important;
}
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
/* hover渐变 */
.login-btn:hover {
  background: linear-gradient(90deg, #094896, #106bc2);
  box-shadow: 0 0 14px #1580e890;
}
/* active按下渐变 */
.login-btn:active {
  background: linear-gradient(90deg, #073b7c, #0c57bb);
  transform: scale(0.98);
}
.row-link {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 16px 0 24px;
  font-size:14px;
}
.link-item {
  color: #69b8ff;
  cursor: pointer;
  display:flex;
  align-items:center;
  gap:4px;
  transition: color 0.2s;
}
.back-arrow{
  font-size:16px;
}
.link-item:hover {
  color: #ffffff;
  text-decoration: none;
}
.footer-text {
  margin-top: auto;
  text-align: center;
  color: #93bfff;
  font-size: 12px;
}
</style>
