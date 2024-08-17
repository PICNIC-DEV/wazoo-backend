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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam("name") String name,
            @RequestParam("loginId") String loginId,
            @RequestParam("loginPassword") String loginPassword,
            @RequestParam("address") String address,
            @RequestParam("language") String language) {
        try {

            System.out.println(">>????12222");

            UserRegistrationDto registrationDto = new UserRegistrationDto();
            registrationDto.setName(name);
            registrationDto.setUserId(loginId);
            registrationDto.setUserPassword(loginPassword);
            registrationDto.setAddress(address);
            registrationDto.setLanguage(language);

            userService.registerUser(registrationDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestParam("userId") String userId,
            @RequestParam("userPassword") String userPassword) {
        try {

            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setUserId(userId);
            loginRequestDto.setUserPassword(userPassword);

            User userDto = userService.login(loginRequestDto);
            return ResponseEntity.ok(userDto);
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

}
