package com.iksun.event.reward.service;

import com.iksun.event.domain.UserPoint;
import com.iksun.event.domain.UserPointHistory;
import com.iksun.event.repository.UserPointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Point 지급 관련 Service
 */
@Slf4j
@Service
public class PointServiceImpl implements PointService {

    @Autowired
    private UserPointRepository userPointRepository;

    /**
     * Point를 유저에게 지급 혹은 회수 한다
     *
     * @param userId
     * @param rewardPoint 0 = -> nothing to do ,
     *                     0 > -> deduct point,
     *                     0 < -> reward point
     * @param eventId 지급 근거
     */
    @Transactional
    @Override
    public void changePoint(String userId, int rewardPoint, int eventId) {
        UserPoint userPoint = this.getUserPoint(userId);

        log.info("changePoint : {} {} {} " , userId, rewardPoint, eventId);
        try {
            int newPoint = userPoint.getPoint() + rewardPoint;
            if (newPoint < 0) { // 만약 추후 지급 점수가 바뀔시 대비
                newPoint = 0;
            }
            userPoint.setPoint(newPoint);
            userPoint.addHistory(makeUserPointHistory(userPoint, rewardPoint, eventId));
            userPointRepository.save(userPoint);
        } catch (Exception e) {
            log.error("CHANGE POINT EXCEPTION", userId, rewardPoint, e);
            throw e;
        }
    }

    @Override
    public UserPoint getUserPoint(String userId) {
        UserPoint userPoint = userPointRepository.findOneByUserId(userId);
        if (userPoint == null) {
            userPoint = UserPoint.getDefaultUserPoint(userId);
        }

        return userPoint;
    }

    private UserPointHistory makeUserPointHistory(UserPoint userPoint, int rewardPoint, int eventId) {
        UserPointHistory userPointHistory = new UserPointHistory();
        userPointHistory.setUserId(userPoint.getUserId());
        userPointHistory.setTotalPoint(userPoint.getPoint());
        userPointHistory.setEarnPoint(rewardPoint); // + 일수도 -일수도 있음
        userPointHistory.setEventId(eventId);
        return userPointHistory;
    }
}
