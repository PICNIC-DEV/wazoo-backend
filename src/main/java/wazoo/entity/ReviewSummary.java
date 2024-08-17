package wazoo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_summary")
@NoArgsConstructor
public class ReviewSummary {
    @Id
    private Integer guideId;
    private Double guideScoreAvg;

    public Integer getGuideId() {
        return guideId;
    }

    public Double getGuideScoreAvg() {
        return guideScoreAvg;
    }
}
