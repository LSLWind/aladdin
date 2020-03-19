package org.phoenix.aladdin.response;

import lombok.Data;

@Data
public class CommonReturnType {
    //返回处理结果, success或fail
    private int code;
    //fail使用通用的错误码格式
    private Object data;

    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,1);
    }
    public static CommonReturnType create(Object result, int code){
        CommonReturnType type=new CommonReturnType();
        type.setCode(code);
        type.setData(result);
        return type;
    }
}
