package org.phoenix.aladdin.controller;

import com.alibaba.fastjson.JSONObject;
import org.phoenix.aladdin.constant.Constant;
import org.phoenix.aladdin.constant.Result;
import org.phoenix.aladdin.model.entity.User;
import org.phoenix.aladdin.model.view.LoginForm;
import org.phoenix.aladdin.model.view.Response;
import org.phoenix.aladdin.service.LoginService;
import org.phoenix.aladdin.service.UserManageService;
import org.phoenix.aladdin.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;


@Controller
@RequestMapping("/web/user")
public class UserController {


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


    /**
     * 用户或员工请求登录
     * @param loginForm
     * @param errors
     * @return
     */
    //TODO 捕获GET抛出的405异常
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> login(@RequestBody @Valid LoginForm loginForm, Errors errors){
        if(errors.hasErrors()){
            return Result.USERNAME_OR_PASSWORD_ERROR;
        }
        //对传来的密码进行MD5加密
        String pwd;
        try {
            pwd=MD5Util.getMD5Str(loginForm.getPwd());
        }catch (Exception e){
            e.printStackTrace();
            return Result.USERNAME_OR_PASSWORD_ERROR;
        }

        //登录校验
        Response response;
        //优先校验员工
        if(loginForm.getPhoneNumber()!=0L){//传递来的是手机号
            response=loginService.checkEmployeeByPhoneNumber(loginForm.getPhoneNumber(),pwd);
        }else {
            response=loginService.checkEmployeeByName(loginForm.getUserName(),pwd);
        }
        //不是员工，校验是不是用户
        if(response==null){
            if(loginForm.getPhoneNumber()!=0L){
                response=loginService.checkUserByPhoneNumber(loginForm.getPhoneNumber(),pwd);
            }else {
                response=loginService.checkUserByName(loginForm.getUserName(),pwd);
            }
        }

        return response==null?Result.USERNAME_OR_PASSWORD_ERROR:new Result<>(Constant.OK,response);
    }

    /**
     * 通过用户id删除用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/deleteUser/{userId}",method = RequestMethod.DELETE)
    @ResponseBody
    public Result<Object> deleteUserById(@PathVariable("userId") long userId){
        boolean success=userManageService.deleteUserById(userId);
        if(success)return Result.DELETE_SUCCESS;
        return Result.DELETE_FAIL_ERROR;
    }

    /**
     * 分页获取用户信息
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/listUser",method = RequestMethod.GET)
    @ResponseBody
    public Result<Object> getUserListByPageAndSize(@PathParam("page")int page,
                                                       @PathParam("size")int size){
        if(page<=0||size<=0)return new Result<>(1,null);

        //页面从0开始，但是传过来的从1开始
        JSONObject data=new JSONObject();
        data.put("userList",userManageService.getAllUserByPageAndSize(page-1,size));
        return new Result<>(1,data);
    }

}
