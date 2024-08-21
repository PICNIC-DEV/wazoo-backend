package wazoo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import wazoo.dto.GuideDetailResponseDto;
import wazoo.dto.SearchRequestDto;
import wazoo.dto.SearchResponseDto;
import wazoo.entity.Guide;
import wazoo.repository.GuideRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        List<String> travelTypes = determineTravelTypes(searchRequestDto.getSelectTravelType());

        List<Guide> guides = new ArrayList<>();
        for (String travelType : travelTypes) {
            guides.addAll(guideRepository.findGuidesWithinRadiusAndType(
                    searchRequestDto.getLatitude(),
                    searchRequestDto.getLongitude(),
                    travelType,
                    5.0  // 반경 5km
            ));
        }

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

    public GuideDetailResponseDto getGuideDetail(Integer guideId) {
        Guide guide = guideRepository.findByGuideId(guideId);

        GuideDetailResponseDto responseDto = new GuideDetailResponseDto();
        responseDto.setGuideId(guide.getGuideId());
        responseDto.setName(guide.getUser().getName());
        responseDto.setProfile(guide.getProfile());
        responseDto.setGuideArea(guide.getActiveArea());
        responseDto.setIntroduction(guide.getIntroduction());
        responseDto.setGuidePrice(guide.getGuidePrice());
        responseDto.setStartDate(guide.getStartDate().toLocalDate());
        responseDto.setEndDate(guide.getEndDate().toLocalDate());

        return responseDto;
    }

    private void generateTravelTypes(JsonNode node, List<String> selectedTypes, int depth, List<List<String>> options, List<String> travelTypes) {
        if (depth == options.size()) {
            String result = node.asText(null);
            if (result != null && !result.isEmpty()) {
                travelTypes.add(result);
            }
            return;
        }

        for (String option : options.get(depth)) {
            generateTravelTypes(node.path(option), selectedTypes, depth + 1, options, travelTypes);
        }
    }

    private List<String> determineTravelTypes(SearchRequestDto.SelectTravelTypeDto travelType) {
        String[] group1Options = {"선택안함", "유동적", "계획적"};
        String[] group2Options = {"선택안함", "플렉스", "가성비"};
        String[] group3Options = {"선택안함", "유명 관광지", "현지인 체험"};
        String[] group4Options = {"선택안함", "혼자", "다같이"};

        List<List<String>> options = new ArrayList<>();
        options.add(getGroupOptions(travelType.getGroup1(), group1Options));
        options.add(getGroupOptions(travelType.getGroup2(), group2Options));
        options.add(getGroupOptions(travelType.getGroup3(), group3Options));
        options.add(getGroupOptions(travelType.getGroup4(), group4Options));

        List<String> travelTypes = new ArrayList<>();
        generateTravelTypes(travelTypeTree, new ArrayList<>(), 0, options, travelTypes);

        return travelTypes;
    }

    private List<String> getGroupOptions(Integer groupValue, String[] options) {
        if (groupValue == 0) {
            // "선택안함"인 경우, 해당 그룹의 모든 옵션을 반환
            return new ArrayList<>(Arrays.asList(Arrays.copyOfRange(options, 1, options.length)));
        } else {
            // 선택된 특정 옵션만 반환
            return List.of(options[groupValue]);
        }
    }
}
