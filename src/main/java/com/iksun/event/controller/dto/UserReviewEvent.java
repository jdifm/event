package com.iksun.event.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserReviewEvent {
    private String type;
    private Action action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;
}
