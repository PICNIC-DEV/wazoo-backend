package wazoo.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class GuideReviewListResponseDto {
    private Integer guideId;
    private Double guideScoreAvg;
    private List<ReviewResponseDto> reviews;

    public GuideReviewListResponseDto(Integer guideId, Double guideScoreAvg, List<ReviewResponseDto> reviews) {
        this.guideId = guideId;
        this.guideScoreAvg = guideScoreAvg;
        this.reviews = reviews;
    }

}
