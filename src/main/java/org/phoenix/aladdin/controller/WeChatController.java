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
import org.phoenix.aladdin.constant.ExpressInfo;
import org.phoenix.aladdin.constant.Result;
import org.phoenix.aladdin.model.entity.City;
import org.phoenix.aladdin.model.entity.Express;
import org.phoenix.aladdin.model.entity.Province;
import org.phoenix.aladdin.model.entity.User;
import org.phoenix.aladdin.model.view.BookingExpressVO;
import org.phoenix.aladdin.service.ExpressService;
import org.phoenix.aladdin.service.GeographyService;
import org.phoenix.aladdin.service.UserService;
import org.phoenix.aladdin.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

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
            System.out.println(response.toString());
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

    /**
     * 传递预约快件信息进行快件预约
     * @param bookingExpressVO 预约快件表单
     * @param session session，获取用户信息
     * @return
     */
    //TODO 信息错误的异常处理
    @RequestMapping(value = "/client/order/mailing",method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> postOrderMailing(BookingExpressVO bookingExpressVO, HttpServletRequest request,
                                           HttpSession session){

        //校验token是否正确
        String token=request.getHeader("token");
        String sessionKey=(String) session.getAttribute("session_key");
        if(token == null || !token.equals(sessionKey)){
            return new Result<>(AppConstant.REQUEST_ILLEGAL_CODE,
                    JSONUtil.oneMessageData(AppConstant.REQUEST_ILLEGAL_MESSAGE));
        }


        //进行预约，信息处理，如果发送方和接收方都不存在则插入数据
        User sender;
        User receiver;
        //发送方并不存在则插入数据，只插入用户名和手机号
        if(!userService.existsByPhoneNumber(bookingExpressVO.getSenderPhoneNumber())){
            sender=new User();
            sender.setName(bookingExpressVO.getSenderName());
            sender.setPhoneNumber(Long.parseLong(bookingExpressVO.getSenderPhoneNumber()));//TODO 手机号应该传递正确的，否则进行一次异常处理
            sender.setPassword(sender.getName());//TODO 密码不能为null，此处先设置为用户名就是密码，尝试获取用户微信密码
            sender=userService.insertOneUser(sender);
        }else {
            sender=userService.getUserByPhoneNumber(Long.parseLong(bookingExpressVO.getSenderPhoneNumber()));
        }
        //接收方并不存在则插入数据，只插入用户名和手机号
        if(!userService.existsByPhoneNumber(bookingExpressVO.getReceiverPhoneNumber())){
            receiver=new User();
            receiver.setName(bookingExpressVO.getReceiverName());
            receiver.setPhoneNumber(Long.parseLong(bookingExpressVO.getReceiverPhoneNumber()));//TODO 手机号应该传递正确的，否则进行一次异常处理
            receiver.setPassword(receiver.getName());
            receiver=userService.insertOneUser(receiver);
        }else {
            receiver=userService.getUserByPhoneNumber(Long.parseLong(bookingExpressVO.getReceiverPhoneNumber()));
        }
        //sender与receiver的作用是提供id，保证用户记录存在
        //TODO 超长的一段快件信息设置.......应该优化设计
        Province province=geographyService.getProvinceByName(bookingExpressVO.getSenderProvince());
        City city=geographyService.getCityByProvinceIdAndName(province.getProvinceId(),bookingExpressVO.getSenderCity());
        Express express=new Express();
        //发送方
        express.setSenderId(sender.getId());
        express.setSenderProvinceId(province.getId());
        express.setSenderCityId(city.getId());
        express.setSenderCountryId(geographyService.getCountryByCityIdAndName(city.getCityId(),bookingExpressVO.getSenderCountry()).getId());
        express.setSenderAddress(bookingExpressVO.getSenderAddress());
        express.setSenderPhoneNumber(sender.getPhoneNumber());
        //接收方
        express.setReceiverId(receiver.getId());
        province=geographyService.getProvinceByName(bookingExpressVO.getReceiverProvince());
        city=geographyService.getCityByProvinceIdAndName(province.getProvinceId(),bookingExpressVO.getReceiverCity());
        express.setReceiverProvinceId(province.getId());
        express.setReceiverCityId(city.getId());
        express.setReceiverCountryId(geographyService.getCountryByCityIdAndName(city.getCityId(),bookingExpressVO.getReceiverCountry()).getId());
        express.setReceiverAddress(bookingExpressVO.getReceiverAddress());
        express.setReceiverPhoneNumber(receiver.getPhoneNumber());
        //其余选项
        express.setBeginTime(bookingExpressVO.getBeginTime());
        express.setEndTime(bookingExpressVO.getEndTime());
        express.setType(BookingExpressVO.TYPE_MAP.get(bookingExpressVO.getType()));
        express.setWeight((float)bookingExpressVO.getKg());
        express.setStatus(ExpressInfo.EXPRESS_MAILING);
        express.setRemark(bookingExpressVO.getRemark());
        express.setMoreInfo(bookingExpressVO.getMoreInfo());

        //插入数据
        express=expressService.insertOneExpress(express);
        if (express==null){
            return new Result<>(AppConstant.SERVER_INSERT_EXCEPTION_CODE,
                    JSONUtil.oneMessageData(AppConstant.SERVER_INSERT_EXCEPTION_MESSAGE));
        }

        return new Result<>(Constant.OK,JSONUtil.oneMessageData(AppConstant.BOOKING_SUCCESS));
    }
}
