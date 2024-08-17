package wazoo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {
    private Integer userNo;
    private Float latitude;
    private Float longitude;
    private SelectTravelTypeDto selectTravelType;

    @Getter
    @Setter
    public static class SelectTravelTypeDto {
        private Integer group1;
        private Integer group2;
        private Integer group3;
        private Integer group4;
    }
}
