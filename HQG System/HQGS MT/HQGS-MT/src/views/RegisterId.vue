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
            <dv-decoration-5 style="width: 400px; height: 40px" class="title-decoration" />
            <!-- 注册标题 -->
            <div class="tab-text">账号注册</div>
            <div class="step-container">
              <!-- ========== 第一步：账号+验证码区域 ========== -->
              <div v-if="step === 1">
                <!-- 账号输入框 -->
                <div class="input-item">
                  <div class="input-wrap">
                    <i class="my-icon input-icon">&#xe63f;</i>
                    <input
                      class="input-box account-input"
                      placeholder="请输入账号"
                      v-model="registerForm.account"
                      @focus="showAccountTip = true"
                      @blur="handleAccountBlur"
                      :disabled="accountDisabled"
                    />
                    <span class="input-suffix">@kyhorizon.com</span>
                  </div>
                  <!-- 单行提示，和找回密码保持一致 -->
                  <div class="msg-line">
                    <span v-if="accountError" class="error-text">{{ accountError }}</span>
                    <span v-else-if="showAccountTip" class="tip-text"
                      >3~18个字符，可使用字母、数字、下划线，需要以字母开头</span
                    >
                  </div>
                </div>
                <!-- 邮箱验证码输入框 + 获取验证码按钮 -->
                <div class="input-item">
                  <div class="input-wrap code-wrap">
                    <i class="my-icon input-icon">&#xe699;</i>
                    <input
                      class="input-box code-input"
                      placeholder="请输入邮箱验证码"
                      v-model="registerForm.code"
                    />
                    <button class="get-code-btn" @click="getCode" :disabled="countDown > 0">
                      {{ countDown > 0 ? `${countDown}s后重发` : '获取验证码' }}
                    </button>
                  </div>
                  <!-- ========= 新增验证码提示行，左对齐 ========= -->
                  <div class="msg-line">
                    <span v-if="codeError" class="error-text">{{ codeError }}</span>
                  </div>
                </div>
              </div>
              <!-- ========== 第二步：密码区域 ========== -->
              <div v-if="step === 2">
                <!-- 账号（只读展示） -->
                <div class="input-item">
                  <div class="input-wrap">
                    <i class="my-icon input-icon">&#xe63f;</i>
                    <input
                      class="input-box account-input"
                      v-model="registerForm.account"
                      disabled
                    />
                    <span class="input-suffix">@kyhorizon.com</span>
                  </div>
                </div>
                <!-- 密码输入框 -->
                <div class="input-item">
                  <div class="input-wrap">
                    <i class="my-icon input-icon">&#xe654;</i>
                    <input
                      class="input-box pwd-input"
                      placeholder="请输入密码"
                      v-model="registerForm.password"
                      :type="pwdVisible ? 'text' : 'password'"
                      @input="validatePwd"
                      @blur="validatePwd"
                    />
                    <div class="pwd-icon-group">
                      <i
                        v-if="registerForm.password"
                        class="my-icon input-clear"
                        @click="registerForm.password = ''"
                        >&#xe602;</i
                      >
                      <i
                        v-if="pwdVisible"
                        class="my-icon input-eye"
                        @click="pwdVisible = !pwdVisible"
                        >&#xe63d;</i
                      >
                      <i v-else class="my-icon input-eye" @click="pwdVisible = !pwdVisible"
                        >&#xe653;</i
                      >
                    </div>
                  </div>
                  <div class="msg-line">
                    <span v-if="pwdError" class="error-text">{{ pwdError }}</span>
                  </div>
                </div>
                <!-- 确认密码 -->
                <div class="input-item">
                  <div class="input-wrap">
                    <i class="my-icon input-icon">&#xe654;</i>
                    <input
                      class="input-box pwd-input"
                      placeholder="请再次输入密码"
                      v-model="registerForm.confirmPwd"
                      :type="confirmPwdVisible ? 'text' : 'password'"
                      @input="validateConfirmPwd"
                      @blur="validateConfirmPwd"
                    />
                    <div class="pwd-icon-group">
                      <i
                        v-if="registerForm.confirmPwd"
                        class="my-icon input-clear"
                        @click="registerForm.confirmPwd = ''"
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
              </div>
            </div>
            <!-- 提示信息，在按钮上方【固定占位，永远存在，不会挤按钮】 -->
            <div class="msg-bottom" :class="msgType">
              {{ msgText || '　' }}
            </div>
            <button class="login-btn" @click="handleRegister">
              {{ step === 1 ? '下一步' : '注册' }}
            </button>
            <!-- 返回登录【添加箭头在文字后面】 -->
            <div class="row-link">
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
import { sendEmailCode, checkEmailCode, register } from '@/api/auth' // 导入接口
const router = useRouter()
// 注册分步：step=1 账号验证码；step=2 设置密码
const step = ref(1)
const accountDisabled = ref(false)
const registerForm = ref({
  account: '',
  code: '',
  password: '',
  confirmPwd: '',
})
const pwdVisible = ref(false)
const confirmPwdVisible = ref(false)
const countDown = ref(0)
const showAccountTip = ref(false)
const accountError = ref('')
const codeError = ref('')
const pwdError = ref('')
const confirmPwdError = ref('')
// 底部提示消息
const msgText = ref('')
const msgType = ref('error') // error / success
// 注册成功倒计时
const countNum = ref(3)
let timer = null

const accountReg = /^[a-zA-Z][a-zA-Z0-9_]{2,17}$/
const pwdReg = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/
// 密码实时校验函数
const validatePwd = () => {
  const val = registerForm.value.password.trim()
  if (!val) {
    pwdError.value = ''
    return false
  }
  if (!pwdReg.test(val)) {
    pwdError.value = '密码至少8个字符，必须包含字母和数字，不能包含空格及特殊符号'
    return false
  }
  pwdError.value = ''
  return true
}
// 确认密码实时校验
const validateConfirmPwd = () => {
  const pwd = registerForm.value.password
  const confirm = registerForm.value.confirmPwd
  if (!confirm) {
    confirmPwdError.value = ''
    return false
  }
  if (pwd !== confirm) {
    confirmPwdError.value = '两次输入密码不一致'
    return false
  }
  confirmPwdError.value = ''
  return true
}
// 监听表单输入，修改输入框自动清空错误提示
watch(
  [
    () => registerForm.value.account,
    () => registerForm.value.code,
    () => registerForm.value.password,
    () => registerForm.value.confirmPwd,
  ],
  () => {
    msgText.value = ''
    codeError.value = ''
    pwdError.value = ''
    confirmPwdError.value = ''
  },
)
watch(
  () => registerForm.value.account,
  (val) => {
    if (val.includes('@')) {
      registerForm.value.account = val.split('@')[0]
    }
    // 输入变化清空错误
    accountError.value = ''
  },
)
// 账号失去焦点：隐藏提示文字，执行校验
const handleAccountBlur = () => {
  showAccountTip.value = false
  validateAccount()
}
// 账号校验函数
const validateAccount = () => {
  const val = registerForm.value.account.trim()
  if (!val) {
    accountError.value = ''
    return false
  }
  if (!accountReg.test(val)) {
    // 【修改】错误文案
    accountError.value = '账号格式错误：3~18字符，字母开头，仅字母数字下划线'
    return false
  }
  accountError.value = ''
  return true
}
// 统一提示方法
const showMsg = (text, type = 'error') => {
  msgText.value = text
  msgType.value = type
}
const getCode = async () => {
  const account = registerForm.value.account.trim()
  if (!account) {
    accountError.value = '请输入账号'
    return
  }
  if (!validateAccount()) return
  // ✅ 点击获取验证码，先清空底部提示文字（解决旧提示残留）
  msgText.value = ''
  try {
    // ========== 修复：增加 type: "register" ==========
    const res = await sendEmailCode({
      username: account,
      type: 'register',
    })
    const result = res.data
    // result = {code:500, message:"xxx", data:null}
    if (result.code === 200) {
      // 接口成功，启动60秒倒计时
      countDown.value = 60
      const timer = setInterval(() => {
        countDown.value--
        if (countDown.value <= 0) {
          clearInterval(timer)
        }
      }, 1000)
    } else {
      // 业务失败：code不等于200，展示后端message
      showMsg(result.message)
    }
  } catch (err) {
    // 捕获axios超时、网络异常
    if (err.message.includes('timeout')) {
      showMsg('服务请求超时，缓存服务异常，请检查Redis是否启动')
    } else {
      let errMsg = '发送失败'
      if (err?.response?.data) {
        if (typeof err.response.data === 'string') {
          errMsg = err.response.data
        } else if (err.response.data.message) {
          errMsg = err.response.data.message
        }
      }
      showMsg(errMsg)
    }
  }
}
// 注册提交（分步逻辑）
const handleRegister = async () => {
  // 第一步：校验账号+验证码，切换到密码页面
  if (step.value === 1) {
    if (!registerForm.value.account) {
      accountError.value = '请输入账号'
      return
    }
    if (!validateAccount()) return
    if (!registerForm.value.code) {
      codeError.value = '请输入邮箱验证码'
      return
    }
    try {
      const res = await checkEmailCode({
        username: registerForm.value.account.trim(),
        code: registerForm.value.code,
        type: 'register', // 新增type参数
      })
      // ========= 修复：取出 res.data 业务返回对象，和getCode逻辑统一 =========
      const result = res.data
      if (result.code !== 200) {
        // 验证码校验失败，错误显示在验证码输入框下方
        codeError.value = result.message
        return
      }
      // 校验成功，切换到第二步
      step.value = 2
      accountDisabled.value = true
      msgText.value = ''
    } catch (err) {
      // 接口异常，错误在验证码下方
      codeError.value = err?.message || '验证码校验失败'
    }
  }
  // 第二步：提交密码完成注册
  else if (step.value === 2) {
    if (!validatePwd()) return
    if (!validateConfirmPwd()) return
    try {
      const res = await register({
        username: registerForm.value.account.trim(),
        password: registerForm.value.password,
        code: registerForm.value.code,
      })
      const result = res.data
      if (result.code === 200) {
        // 注册成功，开启3s倒计时，绿色提示
        countNum.value = 3
        msgType.value = 'success'
        timer = setInterval(() => {
          msgText.value = `注册成功！${countNum.value}s后跳转到登录界面`
          countNum.value--
          if (countNum.value <= 0) {
            clearInterval(timer)
            goLogin()
          }
        }, 1000)
      } else {
        showMsg(result.message)
      }
    } catch (err) {
      showMsg(err?.message || '注册失败')
    }
  }
}
// 返回登录页
const goLogin = () => {
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
/* 和登录页完全一致的渐变发光标题 */
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
/* =========重点新增固定高度容器 =========*/
.step-container {
  height: 200px;
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
  /* 默认左对齐，不用额外设置，和账号提示保持一样 */
}
.tip-text {
  color: #92bfff;
}
.error-text {
  color: #ff7875;
}
/* ========== 验证码行样式 ========== */
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
/* 悬停渐变加深 */
.get-code-btn:hover:not(:disabled) {
  background: linear-gradient(90deg, #094896, #106bc2);
  box-shadow: 0 0 10px #1580e880;
}
/* 点击按下 */
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
/* 禁用状态样式优化 */
.input-box:disabled {
  background: rgba(20, 50, 90, 0.25);
  color: #a0b8d8;
  cursor: not-allowed;
}
.pwd-input {
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
  margin-top: 12px;
}
/* 注册按钮 hover */
.login-btn:hover {
  background: linear-gradient(90deg, #094896, #106bc2);
  box-shadow: 0 0 14px #1580e890;
}
/* 注册按钮点击按下 */
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
}
.split-line {
  color: #69b8ff;
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
}
.footer-text {
  margin-top: auto;
  text-align: center;
  color: #93bfff;
  font-size: 12px;
}
.msg-bottom {
  text-align: center;
  font-size: 14px;
  margin-bottom: 12px;
}
.msg-bottom.error {
  color: #ff7875;
}
.msg-bottom.success {
  color: #73e7a7;
}
</style>
