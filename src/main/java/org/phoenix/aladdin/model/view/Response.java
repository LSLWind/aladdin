package org.phoenix.aladdin.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Response {

    //用户验证成功响应
    private static Response successUserLoginResponse;
    //员工验证成功响应
    private static Response successEmployeeLoginResponse;
    static {
        successUserLoginResponse=new Response("验证成功",0);
        successEmployeeLoginResponse=new Response("验证成功",1);
    }



    private String message;//附加消息

    private int status;//状态码，0为用户，1位员工



    public static Response getSuccessUserLoginResponse(){
        return successUserLoginResponse;
    }

    public static Response getSuccessEmployeeLoginResponse() {
        return successEmployeeLoginResponse;
    }
}
