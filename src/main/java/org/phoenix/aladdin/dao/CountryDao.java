package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryDao extends JpaRepository<Country,Integer> {

    Country findByCityIdAndName(String cityId, String name);
}
