package wazoo.controller;

import wazoo.entity.Guide;
import wazoo.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guides")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @GetMapping("/match")
    public List<Guide> findGuides(@RequestParam String activeArea, @RequestParam Integer travelerUserId) {
        return guideService.findGuidesByRegionAndTitle(activeArea, travelerUserId);
    }

}