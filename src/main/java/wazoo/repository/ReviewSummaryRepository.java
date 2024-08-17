package wazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wazoo.entity.ReviewSummary;

import java.util.Optional;

public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Integer> {
    Double findGuideScoreAvgByGuideId(Integer guideId);
}
