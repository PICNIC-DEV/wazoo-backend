package wazoo.dto;


import lombok.Getter;

@Getter
public class MyPageResponseDto {
    private String name;
    private String travelType;
    private Integer coin;
    private Integer guideId;

    public MyPageResponseDto(String name, String travelType, Integer coin, Integer guideId) {
        this.name = name;
        this.travelType = travelType;
        this.coin = coin;
        this.guideId = guideId;
    }
}
