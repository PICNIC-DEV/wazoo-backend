package wazoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wazoo.dto.SearchRequestDto;
import wazoo.dto.SearchResponseDto;
import wazoo.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping("/search")
    public List<SearchResponseDto> searchGuides(@RequestBody SearchRequestDto searchRequestDto) {
        return searchService.searchGuides(searchRequestDto);
    }
}
