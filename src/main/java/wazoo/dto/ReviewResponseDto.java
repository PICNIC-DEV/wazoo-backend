package wazoo.dto;

import lombok.Getter;
import wazoo.entity.Chat;
import wazoo.entity.Guide;
import wazoo.entity.Review;
import wazoo.entity.User;


// 리뷰 하나당
@Getter
public class ReviewResponseDto {
    private Integer reviewId;

    private User user;

    private Guide guide;

    private Double guideScore;

    private String review;
    private String result;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.user = review.getUser();
        this.guide = review.getGuide();
        this.guideScore = review.getGuideScore();
        this.review = review.getReview();
    }


}
