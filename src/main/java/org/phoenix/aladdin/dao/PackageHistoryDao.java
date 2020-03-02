package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.PackageHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageHistoryDao extends JpaRepository<PackageHistory,Long> {

}
