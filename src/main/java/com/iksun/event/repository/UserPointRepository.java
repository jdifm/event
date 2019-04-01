package com.iksun.event.repository;

import com.iksun.event.domain.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository extends JpaRepository<UserPoint, String> {
    UserPoint findOneByUserId(String userId);

}
