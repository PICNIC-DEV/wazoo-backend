package wazoo.dto;

import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private Integer reviewId;
    private String userTravelType;
    private Double guideScore;
    private String review;

    public ReviewResponseDto(Integer reviewId, String userTravelType, Double guideScore, String review) {
        this.reviewId = reviewId;
        this.userTravelType = userTravelType;
        this.guideScore = guideScore;
        this.review = review;
    }
}
