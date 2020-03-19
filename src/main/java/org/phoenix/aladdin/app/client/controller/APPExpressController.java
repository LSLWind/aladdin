package org.phoenix.aladdin.app.client.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.phoenix.aladdin.app.constant.AppConstant;
import org.phoenix.aladdin.constant.ExpressInfo;
import org.phoenix.aladdin.controller.BaseController;
import org.phoenix.aladdin.error.BusinessException;
import org.phoenix.aladdin.error.EmBusinessError;
import org.phoenix.aladdin.model.entity.Express;
import org.phoenix.aladdin.model.entity.User;
import org.phoenix.aladdin.app.client.viewobject.BookingExpressVO;
import org.phoenix.aladdin.response.CommonReturnType;
import org.phoenix.aladdin.service.ExpressService;
import org.phoenix.aladdin.service.GeographyService;
import org.phoenix.aladdin.service.UserService;
import org.phoenix.aladdin.util.JSONUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
@RequestMapping("/client")
public class APPExpressController extends BaseController {
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
     * 传递预约快件信息进行快件预约
     */
    //TODO 信息错误的异常处理
    @RequestMapping(value = "/order/mailing",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType postOrderMailing(HttpServletRequest request,
                                             HttpSession session,
                                             BookingExpressVO bookingExpressVO){

        //进行预约，信息处理，如果发送方和接收方都不存在则插入数据
        User sender;
        User receiver;
        //发送方并不存在则插入数据，只插入用户名和手机号
        if(!userService.existsByPhoneNumber(bookingExpressVO.getSenderPhoneNumber())){
            sender=new User(bookingExpressVO.getSenderName(),
                    bookingExpressVO.getSenderPhoneNumber(),
                    bookingExpressVO.getSenderName());
            //TODO 手机号应该传递正确的，否则进行一次异常处理
            //TODO 密码不能为null，此处先设置为用户名就是密码，尝试获取用户微信密码
            //设置用户的openid
            sender.setWxOpenid((String)session.getAttribute("openid"));
            sender=userService.insertOneUser(sender);
        }
        //接收方并不存在则插入数据，只插入用户名和手机号
        if(!userService.existsByPhoneNumber(bookingExpressVO.getReceiverPhoneNumber())){
            receiver=new User(bookingExpressVO.getReceiverName(),
                   bookingExpressVO.getReceiverPhoneNumber(),
                    bookingExpressVO.getReceiverName());
            //TODO 手机号应该传递正确的，否则进行一次异常处理，同上
            receiver=userService.insertOneUser(receiver);
        }
        sender=userService.getUserByPhoneNumber(bookingExpressVO.getSenderPhoneNumber());
        receiver=userService.getUserByPhoneNumber(bookingExpressVO.getReceiverPhoneNumber());

        //sender与receiver的作用是提供id，保证用户记录存在
        Express express=new Express();
        BeanUtils.copyProperties(bookingExpressVO,express);//属性复制
        //设置一些不同的字段
        express.setSenderId(sender.getId());
        express.setReceiverId(receiver.getId());
        express.setWeight(bookingExpressVO.getKg());
        express.setStatus(ExpressInfo.EXPRESS_MAILING);
        express.setType(BookingExpressVO.TYPE_MAP.get(bookingExpressVO.getType()));
        System.out.println(express.toString());
        //插入数据
        express=expressService.insertOneExpress(express);
        if (express==null){
            return CommonReturnType.create(JSONUtil.oneMessageData(EmBusinessError.SERVER_EXCEPTION_ERROR.getErrMsg()),
                    EmBusinessError.SERVER_EXCEPTION_ERROR.getErrCode());
        }

        return CommonReturnType.create(JSONUtil.oneMessageData("预约成功"));
    }

    /**
     * 查询订单信息，get
     */
    @RequestMapping(value = "/orders",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType getAllOrders(HttpServletRequest request,HttpSession session) throws BusinessException{
        //从session中获取openid并寻找用户信息
        String openId=(String)session.getAttribute("openid");
        User user=userService.getUserByOpenId(openId);
        //获取订单信息
        JSONObject data=new JSONObject();
        try {
            data.put("sendList",expressService.getSendOrdersByUserId(user.getId()));
            data.put("receiveList",expressService.getReceiveOrdersByUserId(user.getId()));
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(EmBusinessError.SERVER_EXCEPTION_ERROR);
        }

        data.put("msg","suc");

        return CommonReturnType.create(data);
    }

    /**
     * 删除某一订单信息，post
     */
    @RequestMapping(value = "/delOrder",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType deleteOneOrderById(@RequestBody String orderId,HttpServletRequest request,HttpSession session)throws BusinessException{
        if(orderId==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        orderId= (String)JSON.parseObject(orderId).get("orderId");

        if(expressService.deleteExpressById(Long.parseLong(orderId))){
            return CommonReturnType.create(JSONUtil.oneMessageData("suc"));
        }
        return CommonReturnType.create(JSONUtil.oneErrorData(EmBusinessError.DELETE_FAIL_ERROR.getErrMsg())
                ,EmBusinessError.DELETE_FAIL_ERROR.getErrCode());
    }


}
