package wazoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CreateReviewResponseDto {
    private Double guideScore;
    private String review;

}
