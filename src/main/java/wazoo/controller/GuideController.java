package wazoo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import wazoo.dto.GuideDto;
import wazoo.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/guides")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/")
    public Map<String, Boolean> registerGuide(@RequestParam("file") MultipartFile file,
                                              @RequestParam("Info") String infoJson) {
        try {
            // JSON 문자열을 GuideDto로 변환
            GuideDto guideDto = objectMapper.readValue(infoJson, GuideDto.class);

            boolean success = guideService.registerGuide(file, guideDto);
            Map<String, Boolean> response = new HashMap<>();
            response.put("success", success);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Boolean> response = new HashMap<>();
            response.put("success", false);
            return response;
        }
    }

    @PostMapping("/{guideId}")
    public Map<String, Boolean> updateGuide(@PathVariable Integer guideId,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam("Info") String infoJson) {
        try {
            // JSON 문자열을 GuideDto로 변환
            GuideDto guideDto = objectMapper.readValue(infoJson, GuideDto.class);

            boolean success = guideService.updateGuide(guideId, file, guideDto);
            Map<String, Boolean> response = new HashMap<>();
            response.put("success", success);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Boolean> response = new HashMap<>();
            response.put("success", false);
            return response;
        }
    }
}