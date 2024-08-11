package wazoo.repository;

import wazoo.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Integer> {
    List<Guide> findByActiveArea(String activeArea);
    Guide findByGuideId(Integer guideId);
}
