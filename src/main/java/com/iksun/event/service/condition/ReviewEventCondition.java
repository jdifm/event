package com.iksun.event.service.condition;

import com.iksun.event.controller.dto.UserReviewEvent;

import java.util.Map;

public interface ReviewEventCondition {
    /**
     *
     * @param event
     * @return = 0 -> no reward and deduct
     * @return < 0 -> deduct point
     * @return > 0 -> reward point
     */
    int getEventPointResult(UserReviewEvent event, Map<String, Object> extra);
}
