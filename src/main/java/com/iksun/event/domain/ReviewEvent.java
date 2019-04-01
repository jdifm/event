package com.iksun.event.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review_event")
@Data
public class ReviewEvent extends Event {

    @Id
    @Column(name = "review_event_seq_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int reviewEventSeqId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "review_id")
    private String reviewId; // idx

    @Column(name = "place_id")
    private String placeId; // idx

    @Column(name = "display")
    private boolean display; // User가 Review 삭제 시 not display

    @Column(name = "point")
    private int point; // 3 point 합

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="review_event_seq_id")
    private List<ReviewEventHistory> reviewEventHistories;

    public List<ReviewEventHistory> getReviewEventHistories() {
        if (reviewEventHistories == null) {
            reviewEventHistories = new ArrayList<>();
        }

        return reviewEventHistories;
    }

    public void addHistory(ReviewEventHistory reviewEventHistory) {
        if (reviewEventHistories == null) {
            reviewEventHistories = new ArrayList<>();

        }
        reviewEventHistories.add(reviewEventHistory);
    }

    public static ReviewEvent makeNewReview(String userId, String placeId, String reviewId) {
        ReviewEvent reviewEvent = new ReviewEvent();
        reviewEvent.setUserId(userId);
        reviewEvent.setPoint(0);
        reviewEvent.setPlaceId(placeId);
        reviewEvent.setDisplay(true);
        reviewEvent.setReviewId(reviewId);
        return reviewEvent;
    }
}
