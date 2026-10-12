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
            <dv-decoration-5 style="width: 400px; height: 40px" class="title-decoration" />
            <div class="tab-text">找回密码</div>
            <!-- ========== 三步进度条 ========== -->
            <div class="step-wrap">
              <div class="step-item">
                <div class="step-circle" :class="{ active: step >= 1 }">1</div>
                <div class="step-label">身份验证</div>
                <div class="step-desc">邮箱验证</div>
              </div>
              <div class="step-line" :class="{ active: step >= 2 }"></div>
              <div class="step-item">
                <div class="step-circle" :class="{ active: step >= 2 }">2</div>
                <div class="step-label">重置密码</div>
                <div class="step-desc">重新设置密码</div>
              </div>
              <div class="step-line" :class="{ active: step >= 3 }"></div>
              <div class="step-item">
                <div class="step-circle" :class="{ active: step >= 3 }">3</div>
                <div class="step-label">找回成功</div>
                <div class="step-desc">设置成功</div>
              </div>
            </div>
            <!-- ====== 第一步：身份验证 账号+验证码 ====== -->
            <div v-if="step === 1">
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
                <div class="msg-line">
                  <span v-if="accountError" class="error-text">{{ accountError }}</span>
                  <span v-else-if="showAccountTip" class="tip-text"
                    >3~18个字符，可使用字母、数字、下划线，需要以字母开头</span
                  >
                </div>
              </div>
              <div class="input-item">
                <div class="input-wrap code-wrap">
                  <i class="my-icon input-icon">&#xe699;</i>
                  <input
                    class="input-box code-input"
                    placeholder="请输入邮箱验证码"
                    v-model="code"
                  />
                  <button class="get-code-btn" @click="getCode" :disabled="countDown > 0">
                    {{ countDown > 0 ? `${countDown}s后重发` : '获取验证码' }}
                  </button>
                </div>
                <div class="msg-line">
                  <span v-if="codeError" class="error-text">{{ codeError }}</span>
                </div>
              </div>
              <button class="login-btn" @click="nextStep">下一步</button>
            </div>
            <!-- ====== 第二步：重置密码 新密码+确认密码 ====== -->
            <div v-if="step === 2">
              <div class="input-item">
                <div class="input-wrap">
                  <i class="my-icon input-icon">&#xe654;</i>
                  <input
                    class="input-box pwd-input"
                    placeholder="请输入新密码"
                    v-model="newPwd"
                    :type="pwdVisible ? 'text' : 'password'"
                    @input="validatePwd"
                    @blur="validatePwd"
                  />
                  <div class="pwd-icon-group">
                    <i v-if="newPwd" class="my-icon input-clear" @click="newPwd = ''">&#xe602;</i>
                    <i v-if="pwdVisible" class="my-icon input-eye" @click="pwdVisible = !pwdVisible"
                      >&#xe63d;</i
                    >
                    <i v-else class="my-icon input-eye" @click="pwdVisible = !pwdVisible"
                      >&#xe653;</i
                    >
                  </div>
                </div>
                <div class="msg-line">
                  <span v-if="pwdError" class="error-text">{{ pwdError }}</span>
                  <span v-else class="tip-text"
                    >密码至少8个字符，必须包含字母和数字，不能包含空格及特殊符号；</span
                  >
                </div>
              </div>
              <div class="input-item">
                <div class="input-wrap">
                  <i class="my-icon input-icon">&#xe654;</i>
                  <input
                    class="input-box pwd-input"
                    placeholder="请再次输入新密码"
                    v-model="confirmPwd"
                    :type="confirmPwdVisible ? 'text' : 'password'"
                    @input="validateConfirmPwd"
                    @blur="validateConfirmPwd"
                  />
                  <div class="pwd-icon-group">
                    <i v-if="confirmPwd" class="my-icon input-clear" @click="confirmPwd = ''"
                      >&#xe602;</i
                    >
                    <i
                      v-if="confirmPwdVisible"
                      class="my-icon input-eye"
                      @click="confirmPwdVisible = !confirmPwdVisible"
                      >&#xe63d;</i
                    >
                    <i
                      v-else
                      class="my-icon input-eye"
                      @click="confirmPwdVisible = !confirmPwdVisible"
                      >&#xe653;</i
                    >
                  </div>
                </div>
                <div class="msg-line">
                  <span v-if="confirmPwdError" class="error-text">{{ confirmPwdError }}</span>
                </div>
              </div>
              <div style="display: flex; gap: 12px">
                <button class="login-btn back-btn" @click="preStep">上一步</button>
                <button class="login-btn" @click="handleReset">确认重置</button>
              </div>
            </div>
            <!-- ====== 第三步：找回成功 ====== -->
            <div v-if="step === 3" class="success-wrap">
              <div class="success-row-text">密码重置成功！{{ countNum }}s后跳转到登录界面</div>
              <button class="login-btn" @click="goLogin">前往登录</button>
            </div>
            <!-- 返回登录链接：只在 step=1 身份验证页面显示 -->
            <div v-if="step === 1" class="row-link">
              <span class="link-item" @click="goLogin"
                >返回登录 <i class="my-icon back-arrow">&#xe61d;</i></span
              >
            </div>
            <div class="footer-text">版权所有 © 2025 KY-Horizon 自动化技术（天津）有限公司</div>
          </div>
        </dv-border-box-8>
      </div>
    </div>
  </dv-full-screen-container>
</template>
<script setup>
import { ref, watch, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { sendEmailCode, checkEmailCode, resetPassword } from '@/api/auth'
const router = useRouter()
// 步骤 1身份验证 2重置密码 3成功
const step = ref(1)
const account = ref('')
const code = ref('')
const newPwd = ref('')
const confirmPwd = ref('')
const pwdVisible = ref(false)
const confirmPwdVisible = ref(false)
const countDown = ref(0)
const showAccountTip = ref(false)
const accountError = ref('')
const codeError = ref('')
const pwdError = ref('')
const confirmPwdError = ref('')
// 倒计时变量
const countNum = ref(3)
let timer = null
const accountReg = /^[a-zA-Z][a-zA-Z0-9_]{2,17}$/
const pwdReg = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/
// 监听账号输入，自动截断@
watch(account, (val) => {
  if (val.includes('@')) {
    account.value = val.split('@')[0]
  }
  accountError.value = ''
})
watch([() => account.value, () => code.value, () => newPwd.value, () => confirmPwd.value], () => {
  codeError.value = ''
  confirmPwdError.value = ''
})
const handleAccountBlur = () => {
  showAccountTip.value = false
  validateAccount()
}
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
// 密码校验
const validatePwd = () => {
  const val = newPwd.value.trim()
  pwdError.value = ''
  if (!val) {
    return false
  }
  if (!pwdReg.test(val)) {
    pwdError.value = '密码至少8个字符，必须包含字母和数字，不能包含空格'
    return false
  }
  return true
}
// 确认密码校验
const validateConfirmPwd = () => {
  const pwd = newPwd.value
  const confirm = confirmPwd.value
  confirmPwdError.value = ''
  if (!confirm) {
    return false
  }
  if (pwd !== confirm) {
    confirmPwdError.value = '两次输入密码不一致'
    return false
  }
  return true
}
// 获取验证码（重置密码，type=resetPwd）
const getCode = async () => {
  const val = account.value.trim()
  if (!val) {
    accountError.value = '请输入账号'
    return
  }
  if (!validateAccount()) return
  try {
    const res = await sendEmailCode({
      username: val,
      type: 'resetPwd',
    })
    const result = res.data
    if (result.code === 200) {
      countDown.value = 60
      const timer = setInterval(() => {
        countDown.value--
        if (countDown.value <= 0) clearInterval(timer)
      }, 1000)
    } else {
      accountError.value = result.message
    }
  } catch (err) {
    let errMsg = err?.message || '发送验证码失败'
    if (err?.response?.data?.message) {
      errMsg = err.response.data.message
    }
    accountError.value = errMsg
  }
}
const nextStep = async () => {
  if (!validateAccount()) return
  if (!code.value.trim()) {
    codeError.value = '请输入邮箱验证码'
    return
  }
  try {
    const res = await checkEmailCode({
      username: account.value.trim(),
      code: code.value,
      type: 'resetPwd',
    })
    const result = res.data
    if (result.code !== 200) {
      codeError.value = result.message
      return
    }
    // 校验成功，进入第二步
    step.value = 2
  } catch (err) {
    codeError.value = err?.message || '验证码校验失败'
  }
}
const preStep = () => {
  step.value = 1
  newPwd.value = ''
  confirmPwd.value = ''
  pwdError.value = ''
  confirmPwdError.value = ''
}
// 提交重置密码
const handleReset = async () => {
  const val = account.value.trim()
  if (!val) return
  if (!validatePwd()) return
  if (!validateConfirmPwd()) return
  try {
    const res = await resetPassword({
      username: val,
      password: newPwd.value,
      code: code.value,
    })
    const result = res.data
    console.log('reset res', result)
    if (result.code === 200) {
      step.value = 3
      countNum.value = 3
      timer = setInterval(() => {
        countNum.value--
        if (countNum.value <= 0) {
          clearInterval(timer)
          goLogin()
        }
      }, 1000)
    } else {
      pwdError.value = result.message
    }
  } catch (err) {
    console.error('重置密码err', err)
    let errMsg = err?.message || '重置密码失败'
    if (err?.response?.data?.message) {
      errMsg = err.response.data.message
    }
    pwdError.value = errMsg
  }
}
const goLogin = () => {
  // 跳转前清除定时器，防止继续执行
  if (timer) clearInterval(timer)
  router.push('/dashboard/login')
}
// 组件销毁清除定时器，防止内存泄漏
onUnmounted(() => {
  if (timer) clearInterval(timer)
})
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
.tab-text {
  font-size: 26px;
  text-align: center;
  margin-bottom: 24px;
  background: linear-gradient(90deg, #7fc8ff, #ffffff);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  letter-spacing: 4px;
  filter: drop-shadow(0 0 6px #3374cc);
}
.step-wrap {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32px;
}
.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}
.step-circle {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #244477;
  color: #92bfff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  margin-bottom: 6px;
  transition: 0.3s;
}
.step-circle.active {
  background: #1580e8;
  color: #fff;
  box-shadow: 0 0 8px #1580e888;
}
.step-label {
  font-size: 14px;
  color: #c0ddff;
}
.step-desc {
  font-size: 12px;
  color: #88a8d8;
}
.step-line {
  height: 2px;
  background: #244477;
  flex: 1;
  margin: 0 8px;
}
.step-line.active {
  background: #1580e8;
}
.input-item {
  margin-bottom: 20px;
}
.input-wrap {
  position: relative;
  width: 100%;
}
.msg-line {
  margin-top: 4px;
  font-size: 12px;
  line-height: 1.4;
}
.tip-text {
  color: #92bfff;
}
.error-text {
  color: #ff7875;
}
.code-wrap {
  display: flex;
  gap: 10px;
}
.code-input {
  flex: 1;
  padding-right: 10px !important;
}
.get-code-btn {
  width: 130px;
  height: 44px;
  flex-shrink: 0;
  font-size: 14px;
  background: linear-gradient(90deg, #0c57bb, #1580e8);
  border: none;
  color: #fff;
  border-radius: 10px;
  cursor: pointer;
  box-shadow: 0 0 8px #1580e866;
  transition: all 0.2s ease;
}
.get-code-btn:hover:not(:disabled) {
  background: linear-gradient(90deg, #094896, #106bc2);
  box-shadow: 0 0 10px #1580e880;
}
.get-code-btn:active:not(:disabled) {
  background: linear-gradient(90deg, #073b7c, #0c57bb);
  transform: scale(0.98);
}
.get-code-btn:disabled {
  background: #445880;
  cursor: not-allowed;
  box-shadow: none;
  transform: none;
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
  -webkit-appearance: none;
}
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
.back-btn {
  background: #445880;
}
.login-btn:hover {
  background: linear-gradient(90deg, #094896, #106bc2);
  box-shadow: 0 0 14px #1580e890;
}
.login-btn:active {
  background: linear-gradient(90deg, #073b7c, #0c57bb);
  transform: scale(0.98);
}
/* 成功页面 */
.success-wrap {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 30px;
}
/* 修改字号为14px，和注册成功提示统一 */
.success-row-text {
  font-size: 14px;
  color: #4cd284;
}
.row-link {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 16px 0 24px;
  font-size: 14px;
}
.link-item {
  color: #69b8ff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: color 0.2s;
}
.back-arrow {
  font-size: 16px;
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
