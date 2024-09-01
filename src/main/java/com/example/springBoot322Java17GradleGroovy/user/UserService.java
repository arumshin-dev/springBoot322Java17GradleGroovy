package com.example.springBoot322Java17GradleGroovy.user;

import com.example.springBoot322Java17GradleGroovy.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> login(String userId, String password) {
        Map<String, Object> resultMap = new HashMap<>();
//        String encPassword = HashUtil.hash(password);//암호화 패스워드SHA-512
//        System.out.println(encPassword);
//        String saltPassword = HashUtil.hashWithGeneratedSalt(password);
//        System.out.println(saltPassword);
        UserEntity userEntity = new UserEntity();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(userId);
        if(optionalUserEntity.isPresent()) {
            userEntity = optionalUserEntity.get();
            boolean pwChk = HashUtil.verifyHash(password, userEntity.getPassword());
            if(pwChk) {//로그인 성공
                resultMap.put("success", true);
                resultMap.put("message", "로그인에 성공하였습니다.");
                resultMap.put("data", userEntity);
                return resultMap;
            }else{//비밀번호가 일치하지 않음
                resultMap.put("success", false);
                resultMap.put("message", "비밀번호가 일치하지 않습니다.");
                resultMap.put("data", "");
                return resultMap;
            }
        }else{//일치하는 사용자가 없음
            resultMap.put("success", false);
            resultMap.put("message", "해당하는 사용자가 없습니다.");
            resultMap.put("data", "");
            return resultMap;
        }
    }
}
