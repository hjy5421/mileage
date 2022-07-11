package com.triple.mileage.user;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int userPoint;

    public User() {
    }

    public User(String userId, int userPoint) {
        this.userId = userId;
        this.userPoint = userPoint;
    }

    public String getUserId() {
        return userId;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }
}
