package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionDao extends JpaRepository<Region,Long> {
}
