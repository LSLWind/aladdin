package org.phoenix.aladdin.error;

public enum EmBusinessError implements CommonError {
    //通用错误类型
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),
    REQUEST_ILLEGAL_ERROR(10003,"请求非法"),
    SERVER_EXCEPTION_ERROR(10004,"服务器异常"),

    //错误状态码枚举
    //2000开头为用户信息错误
    USER_NOT_EXIST(20001,"用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(20002,"用户名或密码错误"),
    DELETE_FAIL_ERROR(20003,"用户删除失败，请稍后重试"),

    //3000开头为微信错误
    WE_CHAT_LOGIN_STATUS_ERROR(30001,"微信登录异常")
    //4000开头为微信client端错误
    ;
    private EmBusinessError(int errCode, String errMsg){
        this.errCode=errCode;
        this.errMsg=errMsg;
    }
    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode(){
        return errCode;
    }
    @Override
    public String getErrMsg(){
        return errMsg;
    }
    @Override
    public CommonError setErrMsg(String errMsg){
        this.errMsg=errMsg;
        return this;
    }
}
