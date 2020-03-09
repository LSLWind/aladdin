package org.phoenix.aladdin.service;

import org.phoenix.aladdin.dao.CityDao;
import org.phoenix.aladdin.dao.CountryDao;
import org.phoenix.aladdin.dao.ProvinceDao;
import org.phoenix.aladdin.model.entity.City;
import org.phoenix.aladdin.model.entity.Country;
import org.phoenix.aladdin.model.entity.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 地理服务
 * 省市区信息
 */
@Component
public class GeographyService {
    private ProvinceDao provinceDao;
    private CityDao cityDao;
    private CountryDao countryDao;

    @Autowired
    public void setCityDao(CityDao cityDao) {
        this.cityDao = cityDao;
    }
    @Autowired
    public void setCountryDao(CountryDao countryDao) {
        this.countryDao = countryDao;
    }
    @Autowired
    public void setProvinceDao(ProvinceDao provinceDao) {
        this.provinceDao = provinceDao;
    }

    public Province getProvinceByName(String name){
        return provinceDao.findByName(name);
    }

    public City getCityByProvinceIdAndName(String provinceId,String name){
        return cityDao.findByProvinceIdAndName(provinceId,name);
    }

    public Country getCountryByCityIdAndName(String cityId,String name){
        return countryDao.findByCityIdAndName(cityId,name);
    }
}
