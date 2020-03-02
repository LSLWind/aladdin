
package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User,Long> {
    User findByPhoneNumberAndPassword(long phoneNumber,String password);
    User findByNameAndPassword(String name,String password);

    @Modifying
    void deleteById(long userId);
}
