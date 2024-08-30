package com.example.springBoot322Java17GradleGroovy.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //mapper xml
    Optional<UserEntity> findByUserIdAndPassword(String userId, String password);//쿼리
}
