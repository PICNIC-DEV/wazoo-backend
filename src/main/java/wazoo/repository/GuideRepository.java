package wazoo.repository;

import wazoo.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuideRepository extends JpaRepository<Guide, Integer> {
    Guide findByGuideId(Integer guideId);  // GuideId로 가이드를 찾는 메서드
    List<Guide> findAll();  // 모든 가이드를 가져오는 메서드
    List<Guide> findByUserUserNo(Integer userNo);  // UserNo로 가이드를 찾는 메서드

    // 사용자가 선택한 여행지+여행타입에 맞는 가이드 select
    @Query("SELECT g FROM Guide g WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(g.latitude)) * " +
            "cos(radians(g.longitude) - radians(:longitude)) + " +
            "sin(radians(:latitude)) * sin(radians(g.latitude)))) < :radius " +
            "AND g.guideTravelType = :travelType")
    List<Guide> findGuidesWithinRadiusAndType(@Param("latitude") Float latitude,
                                              @Param("longitude") Float longitude,
                                              @Param("travelType") String travelType,
                                              @Param("radius") Double radius);
}
