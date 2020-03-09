package org.phoenix.aladdin.util;

import com.alibaba.fastjson.JSONObject;

/**
 *
 */
public class JSONUtil {
    /**
     * 模板方法，填充只有一个message的data
     * @param message 返回填充的信息
     * @return
     */
    public static Object oneMessageData(String message){
        JSONObject object=new JSONObject();
        object.put("message",message);
        return object;
    }


}
