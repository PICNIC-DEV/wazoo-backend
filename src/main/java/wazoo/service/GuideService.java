package wazoo.service;

import wazoo.entity.Guide;
import wazoo.entity.User;
import wazoo.repository.GuideRepository;
import wazoo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Guide> findGuidesByRegionAndTitle(String activeArea, Integer travelerUserId) {
        User traveler = userRepository.findById(travelerUserId).orElse(null);

        if (traveler == null) {
            return null;
        }

        String travelerType = traveler.getTravelType();

        List<Guide> guides = guideRepository.findByActiveArea(activeArea);

        return guides.stream()
                .filter(guide -> {
                    User guideUser = userRepository.findById(guide.getUser().getUserId()).orElse(null);
                    return guideUser != null && guideUser.getTravelType().equals(travelerType);
                })
                .collect(Collectors.toList());
    }
}

