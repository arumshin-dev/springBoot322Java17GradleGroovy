package com.example.springBoot322Java17GradleGroovy.user;

import com.example.springBoot322Java17GradleGroovy.config.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private View error;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody UserRequest userRequest,
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

        logger.info("id: " + userRequest.getUserId() + ", pw: "+ userRequest.getPassword());

        Map<String, Object> resultMap = new HashMap<>();
        String userId = userRequest.getUserId();
        if(userId == null || userId.isEmpty()) {
            resultMap.put("success", false);
            resultMap.put("message", "사용자ID를 입력해주세요.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
        String password = userRequest.getPassword();
        if(password == null || password.isEmpty()) {
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
    public ResponseEntity<String> test(HttpServletRequest request) {
        // JWT 토큰 검증 시작
        String authorizationHeader = request.getHeader("Authorization"); // 요청 헤더에서 Authorization 헤더 값을 가져옴
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) { // Authorization 헤더가 없거나 Bearer로 시작하지 않으면
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization 헤더가 없거나 잘못되었습니다."); // 401 Unauthorized 응답
        }

        String jwtToken = authorizationHeader.substring(7); // "Bearer " 다음의 토큰 부분만 추출
        boolean tokenChk;
        try {
            tokenChk = JwtUtil.validateToken(jwtToken); // 토큰을 검증
        } catch (ExpiredJwtException e) { // 토큰 만료 시 예외 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다.다시 로그인해 주세요."); // 401 Unauthorized 응답
        } catch (Exception e) { // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다."); // 401 Unauthorized 응답
        }

        if (tokenChk) {
            String userId = JwtUtil.getUserIdFromToken(jwtToken); // 토큰에서 사용자 ID를 추출
            return ResponseEntity.ok(userId); // 정상적으로 사용자 ID를 반환
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다."); // 401 Unauthorized 응답
        }
    }


    @GetMapping("/refreshtoken") // HTTP GET 요청을 처리하는 메서드, URL 경로는 "/refreshToken"
    @Operation(summary = "JWT 토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급.")
    public ResponseEntity<LoginTokenDTO> refreshToken(HttpServletRequest request) { // HTTP 요청 객체를 받음
        try {
            String refreshTokenHeader = request.getHeader("Refresh-Token"); // 요청 헤더에서 리프레시 토큰 값을 가져옴
            if (refreshTokenHeader == null) {
                LoginTokenDTO loginTokenDTO = new LoginTokenDTO(); // 리프레시 토큰이 없으면 응답 객체에 null 설정
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginTokenDTO); // HTTP 401 상태 코드와 함께 응답 반환
            }
            String refreshToken = refreshTokenHeader; // 리프레시 토큰 값 설정
            Boolean tokenChk = JwtUtil.validateToken(refreshToken); // 리프레시 토큰 검증
            if(!tokenChk) {
                LoginTokenDTO loginTokenDTO = new LoginTokenDTO(); // 토큰이 유효하지 않으면 응답 객체에 null 설정
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginTokenDTO); // HTTP 401 상태 코드와 함께 응답 반환
            }
            String userIdFromRefreshToken = JwtUtil.getUserIdFromToken(refreshToken); // 리프레시 토큰에서 사용자 ID 추출

            UserEntity userEntity = userService.getUserByUserId(userIdFromRefreshToken);
            if (userEntity == null) {
                LoginTokenDTO loginTokenDTO = new LoginTokenDTO(); // 저장된 토큰과 일치하지 않으면 응답 객체에 null 설정
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginTokenDTO); // HTTP 401 상태 코드와 함께 응답 반환
            }
            String clientIp = request.getRemoteAddr();
            String newAccessToken = JwtUtil.generateAccessToken(userIdFromRefreshToken, clientIp); // 새로운 액세스 토큰 생성
            LoginTokenDTO loginTokenDTO = new LoginTokenDTO(newAccessToken, null); // 응답 객체에 새로운 액세스 토큰 설정
            return ResponseEntity.ok(loginTokenDTO); // HTTP 200 상태 코드와 함께 응답 반환
        } catch (JwtException e) { // JWT 예외가 발생하면
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // HTTP 401 상태 코드와 함께 응답 반환
        }
    }

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinUser(
            @Parameter(description = "회원가입", required = true) @Valid @RequestBody UserRequest userRequest
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        return ResponseEntity.ok(userService.joinUser(userRequest));
    }

    @GetMapping("/idcheck")
    public ResponseEntity<Map<String, Object>> idCheck(
            @Parameter(description = "사용자아이디", required = true) String id) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean idcheck = userService.checkUserIdDuplicate(id);
        if(!idcheck) {
            resultMap.put("success", true);
            resultMap.put("message", "사용가능한 아이디입니다.");
            resultMap.put("data","");
        }else{
            resultMap.put("success", false);
            resultMap.put("message", "해당 아이디와 동일한 아이디가 존재합니다. 다른 아이디를 사용해주세요.");
            resultMap.put("data","userId");
        }
        return ResponseEntity.ok(resultMap);
    }

    
    @GetMapping("/userinfo")
    @Operation(summary = "회원정보 조회", description = "회원정보 조회")
    public ResponseEntity<Map<String, Object>> userInfo(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        // JWT 토큰 검증 시작
        String authorizationHeader = request.getHeader("Authorization"); // 요청 헤더에서 Authorization 헤더 값을 가져옴
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) { // Authorization 헤더가 없거나 Bearer로 시작하지 않으면
            resultMap.put("success", false);
            resultMap.put("message", "Authorization 헤더가 없거나 잘못되었습니다.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap); // 401 Unauthorized 응답
        }

        String jwtToken = authorizationHeader.substring(7); // "Bearer " 다음의 토큰 부분만 추출
        boolean tokenChk;
        try {
            tokenChk = JwtUtil.validateToken(jwtToken); // 토큰을 검증
        } catch (ExpiredJwtException e) { // 토큰 만료 시 예외 처리
            resultMap.put("success", false);
            resultMap.put("message", "토큰 만료");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap); // 401 Unauthorized 응답
        } catch (Exception e) { // 기타 예외 처리
            resultMap.put("success", false);
            resultMap.put("message", "유효하지 않은 토큰입니다.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap); // 401 Unauthorized 응답
        }

        if (tokenChk) {
            String userId = JwtUtil.getUserIdFromToken(jwtToken); // 토큰에서 사용자 ID를 추출
            UserEntity userEntity = userService.getUserByUserId(userId);
            resultMap.put("success", true);
            resultMap.put("message", "");
            resultMap.put("data", userEntity);
            return ResponseEntity.ok(resultMap);
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "유효하지 않은 토큰입니다.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap); // 401 Unauthorized 응답
        }
    }

    
    @PutMapping("/updateinfo")
    public ResponseEntity<Map<String, Object>> updateInfo(
            @RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        // JWT 토큰 검증 시작
        String authorizationHeader = request.getHeader("Authorization"); // 요청 헤더에서 Authorization 헤더 값을 가져옴
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) { // Authorization 헤더가 없거나 Bearer로 시작하지 않으면
            resultMap.put("success", false);
            resultMap.put("message", "Authorization 헤더가 없거나 잘못되었습니다.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap); // 401 Unauthorized 응답
        }

        String jwtToken = authorizationHeader.substring(7); // "Bearer " 다음의 토큰 부분만 추출
        boolean tokenChk;
        try {
            tokenChk = JwtUtil.validateToken(jwtToken); // 토큰을 검증
        } catch (ExpiredJwtException e) { // 토큰 만료 시 예외 처리
            resultMap.put("success", false);
            resultMap.put("message", "토큰 만료");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap); // 401 Unauthorized 응답
        } catch (Exception e) { // 기타 예외 처리
            resultMap.put("success", false);
            resultMap.put("message", "유효하지 않은 토큰입니다.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap); // 401 Unauthorized 응답
        }

        if (tokenChk) {
            String userId = JwtUtil.getUserIdFromToken(jwtToken); // 토큰에서 사용자 ID를 추출
            UserEntity userEntity = userService.getUserByUserId(userId);
            userEntity = userService.updateUserInfo(userEntity, userUpdateRequest);
            resultMap.put("success", true);
            resultMap.put("message", "회원정보가 수정되었습니다.");
            resultMap.put("data", userEntity);
            return ResponseEntity.ok(resultMap);
        } else {
            resultMap.put("success", false);
            resultMap.put("message", "유효하지 않은 토큰입니다.");
            resultMap.put("data", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap); // 401 Unauthorized 응답
        }
    }
}
