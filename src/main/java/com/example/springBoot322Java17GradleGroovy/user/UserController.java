package com.example.springBoot322Java17GradleGroovy.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest loginRequest
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        String userId = loginRequest.getUserId();
        if(userId == null) {
            resultMap.put("success", false);
            resultMap.put("message", "사용자ID를 입력해주세요.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
        String password = loginRequest.getPassword();
        if(password == null) {
            resultMap.put("success", false);
            resultMap.put("message", "비밀번호를 입력해주세요.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
        resultMap = userService.login(userId, password);
        return ResponseEntity.ok(resultMap);
    }

}
