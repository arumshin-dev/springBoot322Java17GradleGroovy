package com.example.springBoot322Java17GradleGroovy.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    //vo dto

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_sq")
    private long userSq;

    @Column(name = "user_id", length = 30)
    private String userId;

    @Column(name = "password", columnDefinition = "LONGTEXT")
    private String password;

    @Column(name = "email", length = 50)
    private String email;

    public long getUserSq() {
        return userSq;
    }

    public void setUserSq(long userSq) {
        this.userSq = userSq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
