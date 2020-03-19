package org.phoenix.aladdin.service;


import org.phoenix.aladdin.dao.UserAddressDao;
import org.phoenix.aladdin.dao.UserDao;
import org.phoenix.aladdin.dao.UserFeedbackDao;
import org.phoenix.aladdin.model.entity.User;
import org.phoenix.aladdin.model.entity.UserAddress;
import org.phoenix.aladdin.model.entity.UserFeedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private UserFeedbackDao userFeedbackDao;

    @Autowired
    private UserAddressDao userAddressDao;

    /**
     * 通过手机号判断用户是否存在
     * @param phoneNumber 用户手机号
     * @return
     */
    public boolean existsByPhoneNumber(String phoneNumber){
        try {
            return userDao.existsByPhoneNumber(phoneNumber);
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
     * 根据手机号获取用户
     * @param phoneNumber
     * @return
     */
    public User getUserByPhoneNumber(String phoneNumber){
        return userDao.findByPhoneNumber(phoneNumber);
    }

    /**
     * 根据微信openId获取用户
     * @param openId
     * @return
     */
    public User getUserByOpenId(String openId){
        return userDao.findByWxOpenid(openId);
    }

    /**
     * 插入一条用户反馈
     */
    @Transactional
    public UserFeedback insertOneFeedback(UserFeedback userFeedback){
        return userFeedbackDao.save(userFeedback);
    }

    /**
     * 插入一条用户常用地址
     */
    public UserAddress insertOneUserAddress(UserAddress userAddress){
        return userAddressDao.save(userAddress);
    }

    /**
     * 获取用户全部地址列表
     */
    public List<UserAddress> getUserAddressList(long userId){
        return userAddressDao.findByUserId(userId);
    }

    /**
     * 根据用户地址id获取地址
     */
    public UserAddress getAddress(int id){
        return userAddressDao.findById(id);
    }

    /**
     * 更新用户默认地址
     */
    @Transactional
    public boolean updateUserAddressByUserId(long userId,String address){
        try {
            userDao.updateAddress(userId,address);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除用户地址表中的地址
     */
    public boolean deleteUserAddressById(int addressId){
        try {
            userAddressDao.deleteById(addressId);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}

