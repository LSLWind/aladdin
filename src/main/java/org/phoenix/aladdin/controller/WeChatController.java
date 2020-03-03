package org.phoenix.aladdin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.phoenix.aladdin.constant.AppConstant;
import org.phoenix.aladdin.constant.Constant;
import org.phoenix.aladdin.constant.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class WeChatController {

    /**
     * HttpClient是线程安全的
     */
    private static final CloseableHttpClient httpClient= HttpClients.createDefault();
    /**
     * 微信第三方登录，传递一个code，获取session_key(token)
     * @param code 微信服务器传来的code值
     * @return
     */
    @RequestMapping(value = "/client/wxlogin")
    @ResponseBody
    public Result<Object> getWeChatUserSession(@RequestBody String code, HttpSession session){
        //请求登录url
        String url="https://api.weixin.qq.com/sns/jscode2session?" +
                "appid="+ AppConstant.APPID+"&"+"secret="+AppConstant.APPSECRET+"&"+
                "js_code="+code+"&"+"grant_type=authorization_code";
        //构造Get访问
        HttpGet httpGet=new HttpGet(url);
        CloseableHttpResponse response = null;
        //请求链接，返回响应
        try{
            response = httpClient.execute(httpGet);
            //序列化返回对象
            JSONObject data=new JSONObject();
            //获取相应内容
            if(response.getStatusLine().getStatusCode()==200){
                String content= EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONObject object= JSON.parseObject(content);//反序列化
                if(((String)object.get("errcode")).equals("0")){//0即响应成功
                    String openId=(String)object.get("oppenid");
                    String sessionKey=(String)object.get("session_key");
                    session.setAttribute("openid",openId);
                    session.setAttribute("session_key",sessionKey);

                    data.put("token",sessionKey);
                    return new Result<>(Constant.OK,data);
                }else {
                    data.put("token","");
                    return new Result<>(Constant.WE_CHAT_LOGIN_STATUS_ERROR,data);//TODO 失败返回
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response!=null)response.close();
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }
}
