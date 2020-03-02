package org.phoenix.aladdin.service;

import org.phoenix.aladdin.dao.UserDao;
import org.phoenix.aladdin.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.awt.print.Pageable;
import java.util.List;

/**
 * 提供用户管理服务
 */
@Component
public class UserManageService {

    private UserDao userDao;
    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao=userDao;
    }

    /**
     * 根据用户id删除用户
     * @param userId
     * @return
     */
    public boolean deleteUserById(long userId){
        try {
            userDao.deleteById(userId);
        }catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return false;
        }
        return true;
    }

    /**
     * 分页获取所有用户信息
     * @param page
     * @param size
     * @return
     */
    public List<User> getAllUserByPageAndSize(int page,int size){
        PageRequest pageable=PageRequest.of(page,size);
        Slice<User> userSlice=userDao.findAll(pageable);//获取切片
        return userSlice.getContent();
    }
}
