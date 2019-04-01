package com.iksun.event.reward.service;

import com.iksun.event.domain.UserPoint;

public interface PointService {
    UserPoint getUserPoint(String userId);
    void changePoint(String userId, int rewardPoint, int eventId);
}
