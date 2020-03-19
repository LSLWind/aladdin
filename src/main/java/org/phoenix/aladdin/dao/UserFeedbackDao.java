package org.phoenix.aladdin.dao;

import org.phoenix.aladdin.model.entity.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeedbackDao extends JpaRepository<UserFeedback,Integer> {

}
