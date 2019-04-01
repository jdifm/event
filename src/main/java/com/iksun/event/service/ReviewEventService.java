package com.iksun.event.service;

import com.iksun.event.controller.dto.Action;
import com.iksun.event.controller.dto.UserReviewEvent;
import com.iksun.event.domain.ReviewEvent;
import com.iksun.event.domain.ReviewEventHistory;
import com.iksun.event.repository.ReviewEventRepository;
import com.iksun.event.reward.service.PointService;
import com.iksun.event.service.condition.ReviewEventConditionTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReviewEventService implements EventService {

    @Autowired
    private PointService pointPointService;

    @Autowired
    private ReviewEventRepository reviewEventRepository;

    private List<ReviewEventConditionTypes> eventConditions = new LinkedList<>();

    @PostConstruct
    private void setup() {
        eventConditions.add(ReviewEventConditionTypes.REVIEW_CONTENT);
        eventConditions.add(ReviewEventConditionTypes.REVIEW_EVENT_PHOTOS);
        eventConditions.add(ReviewEventConditionTypes.REVIEW_EVENT_FIRST_LOCATION);
    }

    /**
     * 조건이 맞는지 검사하여 포인트 지급
     *
     * @param userReviewEvent
     * @return
     */
    @Transactional
    @Override
    public ReviewEvent joinEvent(UserReviewEvent userReviewEvent) {
        Map<String, Object> extra = new HashMap<>(); // 당첨에 필요한 기타 정보 포함 map
        ReviewEvent myReview = this.getReviewEventOrDefault(userReviewEvent);

        if (this.validateUserReviewEvent(userReviewEvent, myReview) == false) { // 기본 content 혹은 photo 둘중에 하나는 존재
            return myReview;
        }

        extra.put("firstReview", this.getPlaceFirstReview(userReviewEvent.getPlaceId()));
        extra.put("myReview", myReview);

        if (userReviewEvent.getAction() == Action.DELETE) { // delete일 시 추가 작업
            this.disableReviewEvent(myReview);
        }

        for (ReviewEventConditionTypes condition : eventConditions) {
            int rewardPoint = condition.getEventPointResult(userReviewEvent, extra); // 당첨 컨디션 확인하여 지급 포인트 return

            myReview.setPoint(myReview.getPoint() + rewardPoint); // 포인트 변경
            myReview.addHistory(makeReviewEventHistory(myReview, condition, rewardPoint)); // 히스토리 생성
            reviewEventRepository.save(myReview); // 저장

            if (rewardPoint != 0) { // 포인트 변경 시 user_point DB 업데이트
                pointPointService.changePoint(userReviewEvent.getUserId(), rewardPoint, myReview.getReviewEventSeqId());
            }
        }

        return myReview;
    }


    private boolean validateUserReviewEvent(UserReviewEvent userReviewEvent, ReviewEvent reviewEvent) {
        if (userReviewEvent.getAction() != Action.DELETE && (StringUtils.isEmpty(userReviewEvent.getContent()) && CollectionUtils.isEmpty(userReviewEvent.getAttachedPhotoIds()))) { // content나 photo 둘중에 하나는 존재
            return false;
        }

        if (existAnotherReviewInPlace(reviewEvent, userReviewEvent)) { //1장소 1리뷰 정책
            return false;
        }

        // delete 시 처리
        return userReviewEvent.getAction() != Action.DELETE || !CollectionUtils.isEmpty(reviewEvent.getReviewEventHistories());
    }


    private void disableReviewEvent(ReviewEvent reviewEvent) {
        reviewEvent.setDisplay(false);
    }

    private ReviewEventHistory makeReviewEventHistory(ReviewEvent myReview, ReviewEventConditionTypes condition, int rewardPoint) {
        ReviewEventHistory newReviewEventHsitory = null;
        for (ReviewEventHistory existReviewEventHistory : myReview.getReviewEventHistories()) {
            if (existReviewEventHistory.getReviewEventCondition() == condition) {
                if (rewardPoint != 0) {
                    existReviewEventHistory.setPoint(existReviewEventHistory.getPoint() + rewardPoint);
                }
                newReviewEventHsitory = existReviewEventHistory;
                break;
            }
        }

        if (newReviewEventHsitory == null) {
            newReviewEventHsitory = new ReviewEventHistory();
            newReviewEventHsitory.setUserId(myReview.getUserId());
            newReviewEventHsitory.setReviewEventSeqId(myReview.getReviewEventSeqId());
            newReviewEventHsitory.setPoint(rewardPoint);
            newReviewEventHsitory.setReviewEventCondition(condition);
        }
        // reviewEvent HIsotry 만들어 주는 부분 end
        return newReviewEventHsitory;
    }

    private ReviewEvent getPlaceFirstReview(String placeId) {
        ReviewEvent reviewEvent = reviewEventRepository.findFirstByPlaceIdAndDisplayIsTrue(placeId);
        return reviewEvent;
    }

    /**
     * userReviewEvent에서 userId와 placeId로 기존 리뷰 검색 있으면 기존 리뷰, 없으면 신규 생성
     * @param userReviewEvent
     * @return
     */
    private ReviewEvent getReviewEventOrDefault(UserReviewEvent userReviewEvent) {
        ReviewEvent myReview = this.getReviewEvent(userReviewEvent);
        if (myReview == null) {
            myReview = ReviewEvent.makeNewReview(userReviewEvent.getUserId(), userReviewEvent.getPlaceId(), userReviewEvent.getReviewId());
        }

        return myReview;
    }

    /**
     * userId & placeId & disable true인 review_event 가져오기
     * @param userReviewEvent
     * @return
     */
    private ReviewEvent getReviewEvent(UserReviewEvent userReviewEvent) {
        return reviewEventRepository.findOneByUserIdAndPlaceIdAndDisplayIsTrue(userReviewEvent.getUserId(), userReviewEvent.getPlaceId());
    }

    /**
     * 1유저 1장소 1리뷰 기존 리뷰가 있으면 참여 불가능
     * @param reviewEvent
     * @param userReviewEvent
     * @return
     */
    private boolean existAnotherReviewInPlace(ReviewEvent reviewEvent, UserReviewEvent userReviewEvent) {
        return reviewEvent.getReviewId().equals(userReviewEvent.getReviewId()) == false;
    }
}
