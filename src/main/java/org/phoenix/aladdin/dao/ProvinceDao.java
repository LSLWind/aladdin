package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceDao extends JpaRepository<Province,Integer> {
    Province findByName(String name);
}
