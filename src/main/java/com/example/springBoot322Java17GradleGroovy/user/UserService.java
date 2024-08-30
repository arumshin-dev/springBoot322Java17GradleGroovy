package com.example.springBoot322Java17GradleGroovy.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity login(String userId, String password) {
        UserEntity userEntity = new UserEntity();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserIdAndPassword(userId, password);//쿼리 호출
        if (optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
            return userEntity;
        }else {
            return null;
        }
    }
}
