package com.iksun.event.service;

import com.iksun.event.controller.dto.UserReviewEvent;
import com.iksun.event.domain.Event;

public interface EventService {
    Event joinEvent(UserReviewEvent userReviewEvent) throws Exception ;
}
