package wazoo.controller;

import feign.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wazoo.dto.*;
import wazoo.entity.Review;
import wazoo.entity.User;
import wazoo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wazoo.utils.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/join")
    public ResponseEntity<String> registerUser(@RequestBody UserDto user) {
        try {
            UserRegistrationDto registrationDto = new UserRegistrationDto();
            registrationDto.setName(user.getName());
            registrationDto.setUserId(user.getUserId());
            registrationDto.setUserPassword(user.getPassword());
            registrationDto.setAddress(user.getAddress());
            registrationDto.setLanguage(user.getNativeLanguage());
            registrationDto.setRole(user.getRole().toString());

            userService.registerUser(registrationDto);
            return ResponseEntity.ok("User(name : "+user.getName()+") registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            // 사용자 인증 로직
            User user = userService.login(loginRequestDto);
            if (user != null) {
                CustomUserInfoDto customUserInfoDto = new CustomUserInfoDto();
                customUserInfoDto.setUserId(user.getUserId());
                customUserInfoDto.setRole(user.getRole());

                String jwtToken = jwtUtil.createAccessToken(customUserInfoDto);
                return ResponseEntity.ok(new JwtResponse(jwtToken));
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // 1. 리뷰 등록 CreateReviewResponseDto
    @PostMapping("/reviews")
    public ResponseEntity<Map<String, String>> createReview(@RequestBody CreateReviewRequestDto reviewRequestDto) {
        Map<String, String> response = userService.createReview(reviewRequestDto);
        return ResponseEntity.ok(response);
    }

    // 2. 리뷰 수정
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Integer reviewId, @RequestBody UpdateReviewDto updateReviewDto) {
        Review updatedReview = userService.update(reviewId, updateReviewDto);
        return ResponseEntity.ok().body(updatedReview);
    }

    // 3. 리뷰 조회
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReview(@PathVariable Integer reviewId) {
        ReviewResponseDto response = userService.findByReviewId(reviewId);
        return ResponseEntity.ok(response);
    }

    // 4. 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable Integer reviewId){
        userService.deleteReview(reviewId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    // 5. 특정 유저가 작성한 리뷰 조회
    @GetMapping("/{userNo}/reviews")
    public ResponseEntity<UserReviewListResponseDto> getReviewList(@PathVariable Integer userNo) {
        UserReviewListResponseDto reviewsByUserNo = userService.findReviewsByUserNo(userNo);
        return ResponseEntity.ok(reviewsByUserNo);
    }

    // 6. 특정 가이드의 리뷰 리스트 조회
    @GetMapping("/reviews/guides/{guideId}")
    public ResponseEntity<GuideReviewListResponseDto> getGuideReviewList(@PathVariable Integer guideId) {
        GuideReviewListResponseDto guideReviewListResponseDto = userService.findGuideReviewsAndAverage(guideId);
        return ResponseEntity.ok(guideReviewListResponseDto);
    }

    // 마이페이지 조회
    @GetMapping("/{userNo}/mypage")
    public ResponseEntity<MyPageResponseDto> getUserMyPage(@PathVariable Integer userNo) {
        MyPageResponseDto myPageResponseDto = userService.getUserInfoByUserNo(userNo);
        return ResponseEntity.ok(myPageResponseDto);
    }

}

class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
