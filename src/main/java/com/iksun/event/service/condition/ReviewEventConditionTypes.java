package com.iksun.event.service.condition;

import com.iksun.event.controller.dto.Action;
import com.iksun.event.controller.dto.UserReviewEvent;
import com.iksun.event.domain.ReviewEvent;
import com.iksun.event.domain.ReviewEventHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
@Slf4j
public enum ReviewEventConditionTypes implements ReviewEventCondition {

    REVIEW_CONTENT(1) { // 변경된 필요 있을 시 application.properties로 이동

        /**
         * 기존 review가 존재하고 조건에 부합하지 않을 시 - rewardPoint
         * 기존 review가 존재하지 않고 조건에 부합하지 않을 시 0 rewardPoint
         * 기존 review가 존재하지 않고 조건에 부합 시 + rewardPoint
         * @param userReviewEvent
         * @param extra
         * @return
         */
        @Override
        public int getEventPointResult(UserReviewEvent userReviewEvent, Map<String, Object> extra) {
            boolean eventJoinCondition = StringUtils.isEmpty(userReviewEvent.getContent()) == false;
            boolean existReview = this.isExistReview((ReviewEvent) extra.get("myReview"));

            if (eventJoinCondition && existReview == false) {
                return this.rewardPoint;
            } else if (eventJoinCondition == false && existReview) {
                return -this.rewardPoint;
            }

            return 0;
        }
    },

    REVIEW_EVENT_PHOTOS(1) {
        @Override
        public int getEventPointResult(UserReviewEvent event, Map<String, Object> extra) {
            boolean eventJoinCondition = CollectionUtils.isEmpty(event.getAttachedPhotoIds()) == false;
            boolean existReview = this.isExistReview((ReviewEvent) extra.get("myReview"));

            if (eventJoinCondition && existReview == false) {
                return this.rewardPoint;
            } else if (eventJoinCondition == false && existReview) {
                return -this.rewardPoint;
            }

            return 0;
        }
    },

    REVIEW_EVENT_FIRST_LOCATION(1) {
        @Override
        public int getEventPointResult(UserReviewEvent event, Map<String, Object> extra) {
            ReviewEvent firstReviewEvent = (ReviewEvent) extra.get("firstReview");
            ReviewEvent myReview = (ReviewEvent) extra.get("myReview");

            if (firstReviewEvent == null && event.getAction() == Action.ADD) {
                return this.rewardPoint;
            } else if (firstReviewEvent.getReviewId() == myReview.getReviewId() && event.getAction() == Action.DELETE) {
                return -this.rewardPoint;
            }
            return 0;
        }
    };

    protected int rewardPoint; // Reward 지급 대상일 시 지급 포인트

    ReviewEventConditionTypes(int rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    protected boolean isExistReview(ReviewEvent myReview) {
        if (myReview != null) {
            for(ReviewEventHistory history : myReview.getReviewEventHistories()) {
                if (history.getReviewEventCondition() == this && history.getPoint() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getRewardPoint() {
        return rewardPoint;
    }
}
