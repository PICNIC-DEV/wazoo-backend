package wazoo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import wazoo.dto.SearchRequestDto;
import wazoo.dto.SearchResponseDto;
import wazoo.entity.Guide;
import wazoo.repository.GuideRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private GuideRepository guideRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private JsonNode travelTypeTree;

    public SearchService() {
        try {
            travelTypeTree = objectMapper.readTree(new ClassPathResource("types.json").getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SearchResponseDto> searchGuides(SearchRequestDto searchRequestDto) {
        String travelType = determineTravelType(searchRequestDto.getSelectTravelType());

        List<Guide> guides = guideRepository.findGuidesWithinRadiusAndType(
                searchRequestDto.getLatitude(),
                searchRequestDto.getLongitude(),
                travelType,
                5.0  // 반경 5km
        );

        // Guide를 SearchResponseDto로 변환
        List<SearchResponseDto> response = new ArrayList<>();
        for (Guide guide : guides) {
            SearchResponseDto dto = new SearchResponseDto();
            dto.setGuideId(guide.getGuideId());
            dto.setName(guide.getUser().getName());
            dto.setProfile(guide.getProfile());
            dto.setActiveArea(guide.getActiveArea());

            SearchResponseDto.MarkerDto marker = new SearchResponseDto.MarkerDto();
            marker.setLatitude(guide.getLatitude());
            marker.setLongitude(guide.getLongitude());
            marker.setGuidePrice(guide.getGuidePrice());

            dto.setMarker(marker);

            response.add(dto);
        }

        return response;
    }

    private String determineTravelType(SearchRequestDto.SelectTravelTypeDto travelType) {
        String[] group1Options = {"유동적", "계획적"};
        String[] group2Options = {"플렉스", "가성비"};
        String[] group3Options = {"유명 관광지", "현지인 체험"};
        String[] group4Options = {"혼자", "다같이"};

        String type1 = determineGroupType(travelType.getGroup1(), group1Options);
        String type2 = determineGroupType(travelType.getGroup2(), group2Options);
        String type3 = determineGroupType(travelType.getGroup3(), group3Options);
        String type4 = determineGroupType(travelType.getGroup4(), group4Options);

        return travelTypeTree
                .path(type1)
                .path(type2)
                .path(type3)
                .path(type4)
                .asText();
    }

    private String determineGroupType(Integer groupValue, String[] options) {
        return options[groupValue - 1];
    }
}
