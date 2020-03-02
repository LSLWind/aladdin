package org.phoenix.aladdin.service;

import org.phoenix.aladdin.dao.EmployeeDao;
import org.phoenix.aladdin.dao.UserDao;
import org.phoenix.aladdin.model.entity.Employee;
import org.phoenix.aladdin.model.entity.User;
import org.phoenix.aladdin.model.view.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 提供登录，注册，登录校验服务
 */
@Component
public class LoginService {

    private UserDao userDao;
    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao=userDao;
    }

    private EmployeeDao employeeDao;
    @Autowired
    public void setEmployeeDao(EmployeeDao employeeDao){
        this.employeeDao=employeeDao;
    }

    /**
     * 手机号+密码验证用户
     * @param phoneNumber
     * @param password
     * @return 成功登录响应，验证失败返回null
     */
    public Response checkUserByPhoneNumber(long phoneNumber,String password){
        User user=userDao.findByPhoneNumberAndPassword(phoneNumber,password);
        return user==null?null:Response.getSuccessUserLoginResponse();
    }

    /**
     * 用户名+密码验证用户
     * @param name
     * @param password
     * @return 成功登录响应，验证失败返回null
     */
    public Response checkUserByName(String name,String password){
        User user=userDao.findByNameAndPassword(name,password);
        return user==null?null:Response.getSuccessUserLoginResponse();
    }

    /**
     * 员工手机号+密码验证员工
     * @param phoneNumber
     * @param password
     * @return 成功登录响应，验证失败返回null
     */
    public Response checkEmployeeByPhoneNumber(long phoneNumber,String password){
        Employee employee=employeeDao.findByPhoneNumberAndPassword(phoneNumber,password);
        return employee==null?null:Response.getSuccessEmployeeLoginResponse();
    }
    /**
     * 员工名+密码验证员工
     * @param name
     * @param password
     * @return 成功登录响应，验证失败返回null
     */
    public Response checkEmployeeByName(String name,String password){
        Employee employee=employeeDao.findByNameAndPassword(name,password);
        return employee==null?null:Response.getSuccessEmployeeLoginResponse();
    }
}
