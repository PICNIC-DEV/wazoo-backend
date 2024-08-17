package wazoo.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SearchResponseDto {
    private Integer guideId;
    private String name;
    private String profile;
    private String activeArea;
    private MarkerDto marker;

    @Getter
    @Setter
    public static class MarkerDto {
        private Float latitude;
        private Float longitude;
        private Integer guidePrice;
    }
}
