package org.phoenix.aladdin.constant;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    /**
     * 定义一些常用的返回结果
     */
    //用户名或密码错误
    public static final Result<Object> USERNAME_OR_PASSWORD_ERROR;
    //删除数据库数据失败
    public static final Result<Object> DELETE_FAIL_ERROR;
    //删除数据库数据成功
    public static final Result<Object> DELETE_SUCCESS;
    static {
        JSONObject data=new JSONObject();
        data.put("message",Constant.USERNAME_OR_PASSWORD_ERROR);
        data.put("status","");
        USERNAME_OR_PASSWORD_ERROR=new Result<>(Constant.USERNAME_OR_PASSWORD_ERROR_CODE,data);

        data=new JSONObject();
        data.put("message",Constant.DELETE_FAIL_ERROR);
        DELETE_FAIL_ERROR=new Result<>(Constant.DELETE_FAIL,data);

        data=new JSONObject();
        data.put("message",Constant.DELETE_SUCCESS_MESSAGE);
        DELETE_SUCCESS=new Result<>(Constant.OK,data);
    }

    private int code;
    private T data;
}
