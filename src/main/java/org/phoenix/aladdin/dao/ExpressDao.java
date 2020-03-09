package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.Express;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpressDao extends JpaRepository<Express,Long> {
   // @Query("SELECT e from Express e where e.id = ?1")
    Express findById(long id);

    //获取主键最大id，未使用自增主键
    @Query("select max(e.id) from  Express  e")
    int getId();
}
