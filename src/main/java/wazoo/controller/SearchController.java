package wazoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wazoo.dto.GuideDetailResponseDto;
import wazoo.dto.SearchRequestDto;
import wazoo.dto.SearchResponseDto;
import wazoo.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping("/")
    public List<SearchResponseDto> searchGuides(@RequestBody SearchRequestDto searchRequestDto) {
        return searchService.searchGuides(searchRequestDto);
    }
    @GetMapping("/{guideId}")
    public GuideDetailResponseDto getGuideDetail(@PathVariable Integer guideId) {
        return searchService.getGuideDetail(guideId);
    }
}
