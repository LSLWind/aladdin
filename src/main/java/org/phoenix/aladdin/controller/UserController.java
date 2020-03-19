package org.phoenix.aladdin.controller;

import com.alibaba.fastjson.JSONObject;
import org.phoenix.aladdin.error.BusinessException;
import org.phoenix.aladdin.error.EmBusinessError;
import org.phoenix.aladdin.model.view.LoginForm;
import org.phoenix.aladdin.model.view.Response;
import org.phoenix.aladdin.response.CommonReturnType;
import org.phoenix.aladdin.service.LoginService;
import org.phoenix.aladdin.service.UserManageService;
import org.phoenix.aladdin.util.JSONUtil;
import org.phoenix.aladdin.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;


@Controller
@RequestMapping("/web/user")
public class UserController extends BaseController{


    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService){
        this.loginService=loginService;
    }

    private UserManageService userManageService;

    @Autowired
    public void setUserManageService(UserManageService userManageService){
        this.userManageService=userManageService;
    }


    @Autowired
    HttpServletRequest request;

    /**
     * 用户或员工请求登录
     * @param loginForm
     * @param errors
     * @return
     */
    //TODO 捕获GET抛出的405异常
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType login(@RequestBody @Valid LoginForm loginForm, Errors errors) throws BusinessException{
        JSONObject res=new JSONObject();
        if(errors.hasErrors()){
            res.put("error",EmBusinessError.USERNAME_OR_PASSWORD_ERROR.getErrMsg());
            return CommonReturnType.create(res,EmBusinessError.USERNAME_OR_PASSWORD_ERROR.getErrCode());
        }
        //对传来的密码进行MD5加密
        String pwd;
        try {
            pwd=MD5Util.getMD5Str(loginForm.getPwd());
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);//抛出异常
        }

        //登录校验
        Response response;
        //优先校验员工
        if(loginForm.getPhoneNumber()!=null){//传递来的是手机号
            response=loginService.checkEmployeeByPhoneNumber(loginForm.getPhoneNumber(),pwd);
        }else {
            response=loginService.checkEmployeeByName(loginForm.getUserName(),pwd);
        }
        //不是员工，校验是不是用户
        if(response==null){
            if(loginForm.getPhoneNumber()!=null){
                response=loginService.checkUserByPhoneNumber(loginForm.getPhoneNumber(),pwd);
            }else {
                response=loginService.checkUserByName(loginForm.getUserName(),pwd);
            }
        }

        if(response==null){
            res.put("error",EmBusinessError.USERNAME_OR_PASSWORD_ERROR.getErrMsg());
            return CommonReturnType.create(res,EmBusinessError.USERNAME_OR_PASSWORD_ERROR.getErrCode());
        }
        return CommonReturnType.create(response);
    }

    /**
     * 通过用户id删除用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/deleteUser/{userId}",method = RequestMethod.DELETE)
    @ResponseBody
    public CommonReturnType deleteUserById(@PathVariable("userId") long userId){
        boolean success=userManageService.deleteUserById(userId);
        if(success)return CommonReturnType.create(JSONUtil.oneMessageData("删除成功"));
        return CommonReturnType.create(JSONUtil.oneMessageData(EmBusinessError.DELETE_FAIL_ERROR.getErrMsg()),
                EmBusinessError.DELETE_FAIL_ERROR.getErrCode());
    }

    /**
     * 分页获取用户信息
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/listUser",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getUserListByPageAndSize(@RequestParam("page")int page,
                                                       @RequestParam("size")int size){
        if(page<=0||size<=0)return CommonReturnType.create(null);

        //页面从0开始，但是传过来的从1开始
        JSONObject data=new JSONObject();
        data.put("userList",userManageService.getAllUserByPageAndSize(page-1,size));
        return CommonReturnType.create(data);
    }

}
