package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressDao extends JpaRepository<UserAddress,Integer> {
    List<UserAddress> findByUserId(long userId);

    UserAddress findById(int id);

    @Modifying
    void deleteById(int id);
}
