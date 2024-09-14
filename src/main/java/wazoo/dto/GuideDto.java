package wazoo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuideDto {
    private Integer userNo;
    private String introduction;
    private String startDate;
    private String endDate;
    private String activeArea;
    private Float latitude;
    private Float longitude;
    private Integer guidePrice;
    private String guideTravelType;
}