package com.iksun.event.repository;

import com.iksun.event.domain.ReviewEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEventRepository extends JpaRepository<ReviewEvent, Integer> {

    ReviewEvent findFirstByPlaceIdAndDisplayIsTrue(String placeId);
    ReviewEvent findOneByUserIdAndPlaceIdAndDisplayIsTrue(String userId, String placeId);
}
