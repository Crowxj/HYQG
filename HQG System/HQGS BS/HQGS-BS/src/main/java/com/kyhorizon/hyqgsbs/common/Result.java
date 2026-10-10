package com.kyhorizon.hyqgsbs.common;

/**
 * 后端接口统一返回结果封装类
 * @param <T> 泛型，代表返回data的数据类型
 */
public class Result<T> {
    /**
     * 业务状态码
     * 200 成功
     * 400 参数错误
     * 401 未登录 / token失效
     * 403 权限不足
     * 404 资源不存在
     * 500 服务器内部异常
     */
    private Integer code;
    /**
     * 返回提示信息
     */
    private String message;
    /**
     * 返回业务数据
     */
    private T data;

    // 无参构造（序列化需要，JSON转对象必备）
    public Result() {
    }

    // 私有全参构造，禁止外部new，强制使用静态方法创建
    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode () {
        return code;
    }

    public void setCode (Integer code) {
        this.code = code;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public T getData () {
        return data;
    }

    public void setData (T data) {
        this.data = data;
    }

    /**
     * 成功，无返回数据，默认提示【操作成功】
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /**
     * 成功，携带返回数据，默认提示【操作成功】
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /**
     * 成功，自定义提示消息 + 携带数据
     * @param message 自定义提示文本
     * @param data 返回的数据对象
     * @return Result封装对象
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 【新增重载】成功，携带返回数据 + 自定义提示消息（data在前，message在后）
     * @param data 返回的数据对象
     * @param message 自定义提示文本
     * @return Result封装对象
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(200, message, data);
    }

    /**
     * 成功，自定义提示消息，不带数据
     */
    public static <T> Result<T> successMsg(String message) {
        return new Result<>(200, message, null);
    }

    /**
     * 服务器异常 500
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * 自定义状态码 + 自定义消息
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 参数错误 400
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * 未登录 / Token失效 401
     */
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    /**
     * 权限不足 403
     */
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }

    /**
     * 资源不存在 404
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    /**
     * 自定义返回结果，code + message + data
     */
    public static <T> Result<T> build(Integer code, String message, T data) {
        return new Result<>(code, message, data);
    }
}
