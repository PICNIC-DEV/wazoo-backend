package wazoo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateReviewRequestDto {
    private Integer userNo;
    private Integer guideId;
    private Double guideScore;
    private String review;
}
