package com.iksun.event.controller;

import com.iksun.event.controller.dto.UserReviewEvent;
import com.iksun.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class EventController {

    @Autowired
    private EventService reviewEventService;

    @PostMapping("/event")
    public ResponseEntity<Map<String, Object>> postEvent(@RequestBody UserReviewEvent userReviewEvent) {
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");

        try {
            if (userReviewEvent.getType().equals("REVIEW")) {
                result.put("reviewEvent", reviewEventService.joinEvent(userReviewEvent));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("result", "fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // return - 기존 리뷰가 존재
    // return - 리뷰 기입/업데이트/삭제 성공
    // return - exception 발생
}
