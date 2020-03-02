package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.PackageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageContentDao extends JpaRepository<PackageContent,Long> {
    //发现快件所经历的包裹
    List<PackageContent> findByExpressId(long expressId);
}
