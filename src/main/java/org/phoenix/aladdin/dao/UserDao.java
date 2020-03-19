
package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.User;
import org.phoenix.aladdin.model.entity.UserAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    User findByPhoneNumberAndPassword(String phoneNumber,String password);
    User findByNameAndPassword(String name,String password);

    @Modifying
    void deleteById(long userId);

    boolean existsByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    User findByWxOpenid(String openid);

    /**
     * 更新用户地址
     */
    @Modifying
    @Query("update User u set u.address = ?2 where u.id = ?1")
    void updateAddress(long userId,String address);
}
