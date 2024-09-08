package wazoo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import wazoo.RoleType;
import wazoo.dto.*;
import wazoo.entity.Guide;
import wazoo.entity.Review;
import wazoo.entity.ReviewSummary;
import wazoo.entity.User;
import wazoo.repository.GuideRepository;
import wazoo.repository.ReviewRepository;
import wazoo.repository.ReviewSummaryRepository;
import wazoo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewSummaryRepository reviewSummaryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TranslationService translationService;


    public UserService() throws IOException {
    }

    public void registerUser(UserRegistrationDto registrationDto) {
        Optional<User> existingUser = userRepository.findByUserId(registrationDto.getUserId());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();

        user.setName(registrationDto.getName());
        user.setUserId(registrationDto.getUserId());
        user.setUserPassword(passwordEncoder.encode(registrationDto.getUserPassword()));
        user.setAddress(registrationDto.getAddress());
        user.setLanguage(registrationDto.getLanguage());
        user.setRole(RoleType.valueOf(registrationDto.getRole()));

        userRepository.save(user);
    }

    public User login(LoginRequestDto loginRequestDto) {
        return userRepository.findByUserId(loginRequestDto.getUserId())
                .filter(user -> passwordEncoder.matches(loginRequestDto.getUserPassword(), user.getUserPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }

    // 1. 리뷰 등록
    public Map<String, String> createReview(CreateReviewRequestDto reviewRequestDto) {
        User user = userRepository.findByUserNo(reviewRequestDto.getUserNo());
        Guide guide = guideRepository.findByGuideId(reviewRequestDto.getGuideId());
        String guideReview = reviewRequestDto.getReview() == "" ? "등록된 리뷰가 없습니다." : reviewRequestDto.getReview();

        Review review = Review.builder()
                .user(user)
                .guide(guide)
                .guideScore(reviewRequestDto.getGuideScore())
                .review(guideReview)
                .build();

        reviewRepository.save(review);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }

    // 2. 리뷰 수정
    @Transactional
    public Review update(Integer reviewId, UpdateReviewDto updateReviewDto) {
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("not found reviewId: " + reviewId));
        review.update(updateReviewDto.getReview());
        return review;
    }

    // 3. 리뷰 조회
    public ReviewResponseDto findByReviewId(Integer reviewId) {
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() ->
                        new NoSuchElementException("not found reviewId: " + reviewId));

        return new ReviewResponseDto(
                review.getReviewId(),
                review.getGuide().getGuideTravelType(),
                review.getGuideScore(),
                review.getReview()
        );
    }

    // 4. 리뷰 삭제
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // 5. 특정 유저가 작성한 리뷰 조회
    public UserReviewListResponseDto findReviewsByUserNo(Integer userNo){
        List<Review> userReviewsList = reviewRepository.findByUser_UserNo(userNo);

        List<ReviewResponseDto> reviewsDto = userReviewsList.stream()
                .map(review -> new ReviewResponseDto(
                        review.getReviewId(),
                        review.getUser().getTravelType(),
                        review.getGuideScore(),
                        review.getReview()
                        ))
                .collect(Collectors.toList());
        return new UserReviewListResponseDto(reviewsDto);
    }

    // 6. 특정 가이드의 리뷰 리스트와 평균 점수 조회
    public GuideReviewListResponseDto findGuideReviewsAndAverage(Integer guideId, Integer userNo) {
        User user = userRepository.findByUserNo(userNo);
        String userLanguage = user.getLanguage();

        List<ReviewResponseDto> guideReviewsList = getGuideReviews(guideId, userLanguage);
        Double guideScoreAvg = getGuideScoreAvg(guideId);

        return new GuideReviewListResponseDto(guideId, guideScoreAvg, guideReviewsList);
    }

    // 6-1 특정 가이드의 리뷰 리스트 조회 ( 사용자의 언어에 따른 리뷰 번역 추가 )
    private List<ReviewResponseDto> getGuideReviews(Integer guideId, String userLanguage) {
        List<Review> guideReviewsList = reviewRepository.findByGuide_GuideId(guideId);

        List<ReviewResponseDto> reviewsDto = guideReviewsList.stream()
                .map(review -> {
                    String detectedLanguage = translationService.detectLanguage(review.getReview()); // 감지한 언어
                    String reviewText = review.getReview(); // 해당 리뷰 텍스트

                    // 감지된 언어와 사용자의 언어가 다를경우 번역 수행
                    if(!detectedLanguage.equals(userLanguage)) {
                        reviewText = translationService.translateText(review.getReview(), userLanguage, detectedLanguage);
                    }

                    return new ReviewResponseDto(
                            review.getReviewId(),
                            review.getUser().getTravelType(),
                            review.getGuideScore(),
                            reviewText
                    );
                })
                .collect(Collectors.toList());
        return reviewsDto;
    }

    // 6-2 특정 가이드의 평균 점수 조회
    private Double getGuideScoreAvg(Integer guideId) {
        ReviewSummary reviewSummary = reviewSummaryRepository.findById(guideId)
                .orElseThrow(() -> new IllegalArgumentException("not found guideId: " + guideId));
        return reviewSummary.getGuideScoreAvg();
    }

    public MyPageResponseDto getUserInfoByUserNo(Integer userNo) {
        User user = userRepository.findByUserNo(userNo); // 유저 가져오깅
        Guide guide = guideRepository.findByUser(user);
        Integer guideId = (guide != null) ? guide.getGuideId() : null;
        return new MyPageResponseDto(user.getName(), user.getTravelType(), user.getCoin(), guideId);

    }

}