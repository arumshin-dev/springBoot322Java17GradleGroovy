package com.example.springBoot322Java17GradleGroovy.user;

import com.example.springBoot322Java17GradleGroovy.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity login(String userId, String password) {

        String encPassword = HashUtil.hash(password);//암호화 패스워드 SHA-512
        System.out.println(encPassword);
        UserEntity userEntity = new UserEntity();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserIdAndPassword(userId, encPassword);//쿼리 호출
        if (optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
            return userEntity;
        }else {
            return null;
        }
    }
}
