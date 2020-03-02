package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.Employee;
import org.phoenix.aladdin.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao extends JpaRepository<Employee,Long> {
    Employee findByPhoneNumberAndPassword(long phoneNumber, String password);
    Employee findByNameAndPassword(String name,String password);
}
