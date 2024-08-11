package wazoo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wazoo.dto.SurveyRequestDto;
import wazoo.service.SurveyService;

import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/")
    public ResponseEntity<Object> processSurveyResponses(@RequestBody SurveyRequestDto surveyRequestDto) {
        try {
            Map<String, Object> response = surveyService.processResponses(surveyRequestDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
