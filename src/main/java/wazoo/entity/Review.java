package wazoo.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id" )
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name="user_no", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="guide_id", nullable = false)
    private Guide guide;

    @Column(name="guide_score", nullable = false)
    private Double guideScore;

    @Column(name="review")
    private String review;

    @Builder
    public Review(User user, Guide guide, Double guideScore, String review) {
        this.user = user;
        this.guide = guide;
        this.guideScore = guideScore;
        this.review = review;
    }
}
