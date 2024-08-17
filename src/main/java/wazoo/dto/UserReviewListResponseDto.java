package wazoo.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserReviewListResponseDto {
    private List<ReviewResponseDto> reviews;

    public UserReviewListResponseDto(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }
}
