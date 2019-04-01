package com.iksun.event.service;

import com.iksun.event.controller.dto.UserReviewEvent;
import com.iksun.event.domain.ReviewEvent;
import com.iksun.event.domain.ReviewEventHistory;
import com.iksun.event.service.condition.ReviewEventConditionTypes;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ReviewEventConditionTests {

    Map<String, Object>  extra = new HashMap<>();

    @Before
    public void setup() {

    }

    @Test
    public void testREVIEW_CONTENT_PLUS_POINT_NOREVIEW() {
        ReviewEvent myReview = new ReviewEvent();
        UserReviewEvent userReviewEvent = new UserReviewEvent();
        userReviewEvent.setUserId("1");
        userReviewEvent.setContent("TEST");
        int result = ReviewEventConditionTypes.REVIEW_CONTENT.getEventPointResult(userReviewEvent, extra);

        Assert.assertThat(result, CoreMatchers.is(ReviewEventConditionTypes.REVIEW_CONTENT.getRewardPoint()));
    }

    @Test
    public void testREVIEW_EXIST_REVIEW_DELETE_CONTENT_MINUS_POINT() {
        // given 기존 review content 기록이 있었으나 최근 event에 content가 없음
        ReviewEvent myReview = new ReviewEvent();
        myReview.setPoint(ReviewEventConditionTypes.REVIEW_CONTENT.getRewardPoint());
        ReviewEventHistory reviewEventHistoryContent = new ReviewEventHistory();
        reviewEventHistoryContent.setReviewEventCondition(ReviewEventConditionTypes.REVIEW_CONTENT);
        reviewEventHistoryContent.setPoint(ReviewEventConditionTypes.REVIEW_CONTENT.getRewardPoint());
        reviewEventHistoryContent.setUserId("1");
        myReview.addHistory(reviewEventHistoryContent);
        extra.put("myReview", myReview);

        UserReviewEvent userReviewEvent = new UserReviewEvent();
        userReviewEvent.setUserId("1");
        userReviewEvent.setContent("");

        int result = ReviewEventConditionTypes.REVIEW_CONTENT.getEventPointResult(userReviewEvent, extra);
        Assert.assertThat(result, CoreMatchers.is(-ReviewEventConditionTypes.REVIEW_CONTENT.getRewardPoint()));
    }

    @Test
    public void test_REVIEW_PHOTOS() {
        UserReviewEvent userReviewEvent = new UserReviewEvent();
        userReviewEvent.setUserId("1");
        List<String> photos = new ArrayList<>();
        photos.add("TEST");
        userReviewEvent.setAttachedPhotoIds(photos);
        int result = ReviewEventConditionTypes.REVIEW_CONTENT.getEventPointResult(userReviewEvent, extra);
        assertThat(result, CoreMatchers.is(ReviewEventConditionTypes.REVIEW_CONTENT.getRewardPoint()));
    }

    @Test
    public void test_REVIEW_PHOTOS_DELETE_POINT() {
        UserReviewEvent userReviewEvent = new UserReviewEvent();
        userReviewEvent.setUserId("1");
//        List<String> photos = new ArrayList<>();
//        userReviewEvent.setAttachedPhotoIds(photos);

        ReviewEvent myReview = new ReviewEvent();
        myReview.setPoint(ReviewEventConditionTypes.REVIEW_EVENT_PHOTOS.getRewardPoint());
        ReviewEventHistory reviewEventHistoryContent = new ReviewEventHistory();
        reviewEventHistoryContent.setReviewEventCondition(ReviewEventConditionTypes.REVIEW_EVENT_PHOTOS);
        reviewEventHistoryContent.setPoint(ReviewEventConditionTypes.REVIEW_EVENT_PHOTOS.getRewardPoint());
        reviewEventHistoryContent.setUserId("1");
        myReview.addHistory(reviewEventHistoryContent);
        extra.put("myReview", myReview);

        int result = ReviewEventConditionTypes.REVIEW_EVENT_PHOTOS.getEventPointResult(userReviewEvent, extra);
        assertThat(result, CoreMatchers.is(-ReviewEventConditionTypes.REVIEW_EVENT_PHOTOS.getRewardPoint()));
    }

}
