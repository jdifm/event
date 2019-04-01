package com.iksun.event.repository;

import com.iksun.event.domain.ReviewEventHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewEventHistoryRepository extends JpaRepository<ReviewEventHistory, Long> {
}
