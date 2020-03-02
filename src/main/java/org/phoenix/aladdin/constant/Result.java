package org.phoenix.aladdin.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    //用户名或密码错误
    public static final Result<String> NAME_OR_PASSWORD_ERROR;
    //删除数据库数据失败
    public static final Result<String> DELETE_FAIL_ERROR;
    //删除数据库数据成功
    public static final Result<String> DELETE_SUCCESS;
    static {
        NAME_OR_PASSWORD_ERROR=new Result<>(Constant.USERNAME_OR_PASSWORD_ERROR_CODE,Constant.USERNAME_OR_PASSWORD_ERROR);
        DELETE_FAIL_ERROR=new Result<>(Constant.DELETE_FAIL,Constant.DELETE_FAIL_ERROR);
        DELETE_SUCCESS=new Result<>(Constant.OK,Constant.DELETE_SUCCESS_MESSAGE);
    }

    private int code;
    private T data;
}
