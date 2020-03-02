package org.phoenix.aladdin.dao;

import org.junit.jupiter.api.Test;
import org.phoenix.aladdin.model.entity.Express;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ExpressDaoTest {

    @Autowired
    ExpressDao expressDao;
    @Test
    void findById() {
        Express express=expressDao.findById(1L);
        System.out.println(express);
    }
}