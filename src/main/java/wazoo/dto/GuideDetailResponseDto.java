package wazoo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GuideDetailResponseDto {
    private Integer guideId;
    private String name;
    private String profile;
    private String guideArea;
    private String introduction;
    private Integer guidePrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReviewsUrl reviewsUrl;

    @Getter
    @Setter
    public static class ReviewsUrl {
        private String rel;
        private String href;
        private String action;
    }
}
