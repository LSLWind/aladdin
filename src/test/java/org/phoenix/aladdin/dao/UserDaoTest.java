package org.phoenix.aladdin.dao;

import org.junit.jupiter.api.Test;
import org.phoenix.aladdin.model.entity.Region;
import org.phoenix.aladdin.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    @Autowired
    UserDao userDao;

    @Autowired
    RegionDao regionDao;

    @Test
    public void dbTest(){
        Region region=new Region();
        region.setId(1);
        region.setProvince("河南省");
        region.setCity("信阳市");
        region.setRegion("龙虾区");
        regionDao.save(region);

        User user=new User();
        user.setId(1L);
        user.setName("lsl");
        user.setPassword("123456");
        user.setPhoneNumber(13849720276L);
        user.setRegionId(1);
        userDao.save(user);
        System.out.println(userDao.findAll().get(0).toString());
    }
}