package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityDao extends JpaRepository<City,Integer> {
    City findByProvinceIdAndName(String provinceId, String name);
}
