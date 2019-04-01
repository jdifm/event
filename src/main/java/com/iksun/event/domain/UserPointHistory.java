package com.iksun.event.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_point_history")
@Data
public class UserPointHistory {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "seq_id")
    private int seqId;

    @Column(name = "user_id", nullable = true)
    private String userId;

    @Column(name = "total_point")
    private int totalPoint; // 증가 / 감소 후 포인트 총량

    @Column(name = "earn_point")
    private int earnPoint;  // 증가 / 감소 포인트양

    @Column(name = "event_id")
    private int eventId;        // 증가 / 감소의 근거

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;}
