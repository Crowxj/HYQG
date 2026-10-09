import axios from 'axios'
const service = axios.create({
  // baseURL: '/api',
  baseURL: 'http://localhost:8080',
  timeout: 10000
})

// 请求拦截器：自动注入token
service.interceptors.request.use(
  config => {
    //优先拿localStorage，再拿sessionStorage
    const token = localStorage.getItem('token') || sessionStorage.getItem('token')
    if(token){
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

//响应拦截器，统一处理错误
service.interceptors.response.use(
  res => res,
  err => {
    //401 token失效，清空存储跳回登录
    if(err.response && err.response.status === 401){
      localStorage.removeItem('token')
      sessionStorage.removeItem('token')
      location.href="/login"
    }
    return Promise.reject(err)
  }
)
export default service
