import request from '@/utils/request'

// 获取验证码
export function sendEmailCode(data) {
  return request({
    url: '/api/dashboard/sendEmailCode',
    method: 'post',
    data,
  })
}

//校验验证码
export function checkEmailCode(data) {
  return request({
    url: '/api/dashboard/checkEmailCode',
    method: 'post',
    data,
  })
}

//注册
export function register(data) {
  return request({
    url: '/api/dashboard/register',
    method: 'post',
    data,
  })
}

// 登录
export function login(data) {
  return request({
    url: '/api/dashboard/login',
    method: 'post',
    data,
  })
}

// 重置密码
export function resetPassword(data) {
  return request({
    url: '/api/dashboard/resetPassword',
    method: 'post',
    data,
  })
}
