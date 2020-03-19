package org.phoenix.aladdin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.phoenix.aladdin.app.client.viewobject.LoginCodeVO;
import org.phoenix.aladdin.app.constant.AppConstant;
import org.phoenix.aladdin.error.BusinessException;
import org.phoenix.aladdin.error.EmBusinessError;
import org.phoenix.aladdin.model.entity.User;
import org.phoenix.aladdin.response.CommonReturnType;
import org.phoenix.aladdin.service.ExpressService;
import org.phoenix.aladdin.service.GeographyService;
import org.phoenix.aladdin.service.UserService;
import org.phoenix.aladdin.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
public class WeChatController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private GeographyService geographyService;

    @Autowired
    public void setGeographyService(GeographyService geographyService) {
        this.geographyService = geographyService;
    }

    private ExpressService expressService;

    @Autowired
    public void setExpressService(ExpressService expressService) {
        this.expressService = expressService;
    }


    /** 全局连接池对象，静态代码块配置连接池信息*/
    private static final PoolingHttpClientConnectionManager connManager;
    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        connManager =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(20);
    }
    /**
     * HttpClient是线程安全的
     */
    private static final CloseableHttpClient httpClient= HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();

    /**
     * 微信第三方登录，传递一个code，获取session_key(token)
     * @return
     */
    @RequestMapping(value = "/client/wxlogin")
    @ResponseBody
    public CommonReturnType getWeChatUserSession(@RequestBody LoginCodeVO loginCodeVO, HttpSession session) throws BusinessException, IOException {
        String code=loginCodeVO.getCode();
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
                if(content!=null){
                    //判断是否存在错误信息
                    String errMsg=(String)object.get("errmsg");
                    if(errMsg!=null){
                        return CommonReturnType.create(JSONUtil.oneErrorData(errMsg),
                                (Integer) object.get("errcode"));
                    }
                    //无错，存储信息
                    String openId=(String)object.get("openid");
                    String sessionKey=(String)object.get("session_key");
                    session.setAttribute("openid",openId);
                    session.setAttribute("session_key",sessionKey);
                    System.out.println(object);
                    data.put("token",sessionKey);
                    //判断用户是否首次登陆，不是则存储用户信息
                    //TODO 此处可多线程进行异步信息处理
                    User user=userService.getUserByOpenId(openId);
                    if(user==null){
                        user=new User();
                        user.setWxOpenid(openId);
                        //TODO 获取用户其余信息
                        userService.insertOneUser(user);
                    }
                    return CommonReturnType.create(data);
                }else {
                    data.put("token","");
                    data.put("error", EmBusinessError.WE_CHAT_LOGIN_STATUS_ERROR.getErrMsg());
                    return CommonReturnType.create(data,EmBusinessError.WE_CHAT_LOGIN_STATUS_ERROR.getErrCode());//TODO 失败返回
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }finally {
            try {
                if(response!=null)response.close();//关闭响应集，因为连接池有限
                httpClient.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }


}
