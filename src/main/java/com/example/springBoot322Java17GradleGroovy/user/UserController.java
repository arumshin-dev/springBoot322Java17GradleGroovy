package com.example.springBoot322Java17GradleGroovy.user;

import com.example.springBoot322Java17GradleGroovy.config.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ) {

        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String device = "";
        if (userAgent == null) {
            device = "Unknown";
        }

        if (userAgent.contains("Windows")) {
            device = "Windows PC";
        }else if (userAgent.contains("iPhone")) {
            device = "iPhone";
        } else if (userAgent.contains("iPad")) {
            device = "iPad";
        }  else if (userAgent.contains("Mac OS")) {
            device = "Mac PC";
        } else if (userAgent.contains("Android")) {
            if (userAgent.contains("Mobile")) {
                device = "Android Phone";
            } else {
                device = "Android Tablet";
            }
        } else {
            device = "Unknown Device";
        }

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
        resultMap = userService.login(userId, password, clientIp);
        return ResponseEntity.ok(resultMap);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        // JWT 토큰 검증 시작
        String authorizationHeader = request.getHeader("Authorization"); // 요청 헤더에서 Authorization 헤더 값을 가져옴
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) { // Authorization 헤더가 없거나 Bearer로 시작하지 않으면
            return "";
        }
        String jwtToken = authorizationHeader.substring(7); // "Bearer " 다음의 토큰 부분만 추출
        boolean tokenChk = JwtUtil.validateToken(jwtToken);
        if(tokenChk) {
            String userId = JwtUtil.getUserIdFromToken(jwtToken); // 토큰에서 사용자 ID를 추출
            JwtUtil.invalidateToken(userId);//토큰 만료 로그아웃
            return userId;
        }else{
            return "토큰 만료";
        }
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        // JWT 토큰 검증 시작
        String authorizationHeader = request.getHeader("Authorization"); // 요청 헤더에서 Authorization 헤더 값을 가져옴
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) { // Authorization 헤더가 없거나 Bearer로 시작하지 않으면
            return "";
        }
        String jwtToken = authorizationHeader.substring(7); // "Bearer " 다음의 토큰 부분만 추출
        boolean tokenChk = JwtUtil.validateToken(jwtToken);
        if(tokenChk) {
            String userId = JwtUtil.getUserIdFromToken(jwtToken); // 토큰에서 사용자 ID를 추출
            return userId;
        }else{
            return "토큰 만료";
        }
    }
}
