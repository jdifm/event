package com.iksun.event.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_point")
@Data
public class UserPoint {

    @Id
    @Column(name = "user_id", nullable = true)
    private String userId;

    @Column(name = "point")
    private int point;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    List<UserPointHistory> userPointHistoryList;

    public static UserPoint getDefaultUserPoint(String userId) {
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(userId);
        userPoint.setPoint(0);
        return userPoint;
    }

    public void addHistory(UserPointHistory history) {
        if (userPointHistoryList == null) {
            userPointHistoryList = new ArrayList<>();
        }
        userPointHistoryList.add(history);
    }
}
