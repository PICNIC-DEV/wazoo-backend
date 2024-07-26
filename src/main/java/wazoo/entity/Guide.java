package wazoo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Entity
@Table(name = "guide")
@Getter
@Setter
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_id")
    private int guideId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "price")
    private int price;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "active_area")
    private String activeArea;

    @Column(name = "certificate_type")
    private String certificateType;

    @Column(name = "is_popular")
    private boolean isPopular;

    @Column(name = "is_recommend")
    private boolean isRecommend;

    @Column(name = "latitude")
    private float latitude;

    @Column(name = "longitude")
    private float longitude;
}
