package wazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wazoo.dto.ReviewResponseDto;
import wazoo.entity.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByReviewId(Integer reviewId);
    List<Review> findByUser_UserNo(Integer reviewNo);
    List<Review> findByGuide_GuideId(Integer guideId);
}
