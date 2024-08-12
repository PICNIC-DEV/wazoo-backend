package wazoo.repository;

import wazoo.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Integer> {
    Guide findByGuideId(Integer guideId);  // GuideId로 가이드를 찾는 메서드
    List<Guide> findAll();  // 모든 가이드를 가져오는 메서드
    List<Guide> findByUserUserNo(Integer userNo);  // UserNo로 가이드를 찾는 메서드
}
