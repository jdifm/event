package com.iksun.event.domain;

import com.iksun.event.service.condition.ReviewEventConditionTypes;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Review_event 테이블 생성 시 각 condition에 따른 review_event_history 생성
 */
@Entity
@Table(name = "review_event_history")
@Data
public class ReviewEventHistory {

    @Id
    @Column(name ="review_event_history_seq_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String reviewEventHistorySeqId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "review_event_seq_id")
    private int reviewEventSeqId; // review event의 id

    @Enumerated(EnumType.STRING)
    @Column(name = "review_event_condition")
    private ReviewEventConditionTypes reviewEventCondition; // review Event의 조건

    @Column(name = "point")
    private int point; // 조건에 부합하여 지급된 혹은 회수된 포인트

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

}
