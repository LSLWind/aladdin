package org.phoenix.aladdin.app.client.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.phoenix.aladdin.app.client.viewobject.PhoneLoginVO;
import org.phoenix.aladdin.app.client.viewobject.UserAddressVO;
import org.phoenix.aladdin.controller.BaseController;
import org.phoenix.aladdin.error.BusinessException;
import org.phoenix.aladdin.error.EmBusinessError;
import org.phoenix.aladdin.model.entity.User;
import org.phoenix.aladdin.model.entity.UserAddress;
import org.phoenix.aladdin.model.entity.UserFeedback;
import org.phoenix.aladdin.response.CommonReturnType;
import org.phoenix.aladdin.service.UserService;
import org.phoenix.aladdin.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
@RequestMapping("/client")
public class APPUserController extends BaseController {
    @Autowired
    private UserService userService;

    /**
     * 返回四位数的验证码，存于session中
     */
    @RequestMapping("/phoneVerifyCode")
    @ResponseBody
    public CommonReturnType sendPhoneVerifyCode(HttpServletRequest request,HttpSession session){
        //随机数作为验证码
        Random random=new Random();
        int randomInt=random.nextInt(9999)+1000;
        String verifyCode=String.valueOf(randomInt);
        //session绑定验证码
        session.setAttribute("verifyCode",verifyCode);
        //返回对象
        JSONObject data=new JSONObject();
        data.put("msg","suc");
        data.put("verifyCode",verifyCode);
        return CommonReturnType.create(data);
    }

    /**
     * 用户手机号登录，携带code，实际上还是调用微信登录
     */
    @RequestMapping("/phoneLogin")
    @ResponseBody
    public CommonReturnType getUserPhoneLogin(@RequestBody PhoneLoginVO phoneLoginVO, HttpServletRequest request,HttpSession session, HttpServletResponse response) throws BusinessException {
        if(phoneLoginVO==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        //用户手机号不存在则插入用户
        if(!userService.existsByPhoneNumber(phoneLoginVO.getPhoneNumber())){
            User user=new User(phoneLoginVO.getPhoneNumber(),phoneLoginVO.getPhoneNumber(),phoneLoginVO.getPhoneNumber());
            userService.insertOneUser(user);
        }
        session.setAttribute("phoneNumber",phoneLoginVO.getPhoneNumber());

        //通过code获取微信登录状态
        //TODO 此处可能会识别不了附带信息
        request.setAttribute("code",phoneLoginVO.getCode());
        RequestDispatcher requestDispatcher=request.getRequestDispatcher("/wxlogin");
        try {
            requestDispatcher.forward(request,response);
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(EmBusinessError.SERVER_EXCEPTION_ERROR);
        }

        //转发不成功返回登录失败
        return CommonReturnType.create(JSONUtil.oneErrorData(EmBusinessError.WE_CHAT_LOGIN_STATUS_ERROR.getErrMsg()),
                EmBusinessError.WE_CHAT_LOGIN_STATUS_ERROR.getErrCode());
    }

    /**
     * 用户反馈信息
     */
    @RequestMapping(value = "/feedback",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType postUserFeedback(@RequestBody String feedback,HttpServletRequest request,HttpSession session) throws BusinessException{
        if(feedback==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        //反序列化json数据
        feedback=(String)JSON.parseObject(feedback).get("feedback");
        //由openid获取用户
        User user=userService.getUserByOpenId((String)session.getAttribute("openid"));
        //插入用户反馈
        UserFeedback userFeedback=new UserFeedback();
        userFeedback.setUserId(user.getId());
        userFeedback.setFeedback(feedback);
        userService.insertOneFeedback(userFeedback);

        return CommonReturnType.create(JSONUtil.oneMessageData("suc"));
    }

    /**
     * 用户退出登录
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType postLogout(HttpSession session,HttpServletRequest request){
        session.invalidate();//设置session无效

        return CommonReturnType.create(JSONUtil.oneMessageData(""));
    }


    /**
     * 新增用户地址
     */
    @RequestMapping(value = "/addAddress",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType postAddUserAddress(@RequestBody UserAddressVO userAddressVO,HttpServletRequest request,
                                               HttpSession session)throws BusinessException{
        if(userAddressVO==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        //查询用户
        User user=userService.getUserByPhoneNumber(userAddressVO.getPhoneNumber());
        //创建用户地址
        UserAddress userAddress=new UserAddress();
        userAddress.setUserId(user.getId());
        userAddress.setAddress(userAddressVO.getNewAddress());
        //插入
        userService.insertOneUserAddress(userAddress);

        return CommonReturnType.create(JSONUtil.oneMessageData("suc"));
    }

    /**
     * 获取用户地址列表
     */
    @RequestMapping(value = "/getAddrlist",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType getUserAddressList(HttpServletRequest request,HttpSession session) throws BusinessException{
        //由openid获取用户
        User user=userService.getUserByOpenId((String)session.getAttribute("openid"));

        JSONObject data=new JSONObject();
        try {
            data.put("addressList",userService.getUserAddressList(user.getId()));
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(EmBusinessError.SERVER_EXCEPTION_ERROR);
        }
        data.put("msg","suc");
        return CommonReturnType.create(data);
    }

    /**
     * 设置用户默认地址
     */
    @RequestMapping(value = "/setDefaultAddr",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType postSetDefaultAddress(@RequestBody String addressId,HttpServletRequest request,
                                                  HttpSession session) throws BusinessException{
        if(addressId==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        //反序列化json数据
        addressId=(String)JSON.parseObject(addressId).get("addressId");
        //由openid获取用户
        User user=userService.getUserByOpenId((String)session.getAttribute("openid"));
        //由地址id获取地址
        UserAddress userAddress=userService.getAddress(Integer.parseInt(addressId));
        if(userAddress==null)return CommonReturnType.create(JSONUtil.oneErrorData("该地址不存在"));
        //更新用户地址
        if (userService.updateUserAddressByUserId(user.getId(),userAddress.getAddress())){
            return CommonReturnType.create(JSONUtil.oneMessageData(""));
        }else {
            throw new BusinessException(EmBusinessError.SERVER_EXCEPTION_ERROR);
        }
    }

    /**
     * 从用户地址表删除用户地址
     */
    @RequestMapping(value = "/delAddress",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType deleteAddress(@RequestBody String addressId,HttpServletRequest request,
                                          HttpSession session)throws BusinessException{
        if(addressId==null)throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        //反序列化json数据
        addressId=(String)JSON.parseObject(addressId).get("addressId");

        if(userService.deleteUserAddressById(Integer.parseInt(addressId))){
            return CommonReturnType.create(JSONUtil.oneMessageData(""));
        }
        return CommonReturnType.create(JSONUtil.oneErrorData("删除失败"));
    }

}
