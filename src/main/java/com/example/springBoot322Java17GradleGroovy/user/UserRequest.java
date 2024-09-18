package com.example.springBoot322Java17GradleGroovy.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String userId;
    private String password;
    private String email;

//    public UserRequest() {}
    public UserRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}
