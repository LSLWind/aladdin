package org.phoenix.aladdin.service;

import org.phoenix.aladdin.dao.UserDao;
import org.phoenix.aladdin.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * 提供用户操作服务
 */
@Component
public class UserService {
    private UserDao userDao;
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 通过手机号判断用户是否存在
     * @param phoneNumber 用户手机号
     * @return
     */
    public boolean existsByPhoneNumber(String phoneNumber){
        try {
            return userDao.existsByPhoneNumber(Long.parseLong(phoneNumber));
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 插入用户数据
     * @param user
     * @return 插入后的用户
     */
    @Transactional
    public User insertOneUser(User user){
        return userDao.save(user);
    }

    /**
     * 根据手机号获取用户xinxi
     * @param phoneNumber
     * @return
     */
    public User getUserByPhoneNumber(long phoneNumber){
        return userDao.findByPhoneNumber(phoneNumber);
    }
}
