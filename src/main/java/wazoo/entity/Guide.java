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
    private Integer guideId;

    @ManyToOne
    @JoinColumn(name = "user_no", referencedColumnName = "user_no", nullable = false)
    private User user;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @Column(name = "active_area", nullable = false)
    private String activeArea;

    @Column(name = "latitude", nullable = false)
    private Float latitude;

    @Column(name = "longitude", nullable = false)
    private Float longitude;

    @Column(name = "guide_price", nullable = false)
    private Integer guidePrice;

    @Column(name = "guide_travel_type", nullable = false)
    private String guideTravelType;

    @Column(name = "profile", nullable = false)
    private String profile;

    @Column(name = "certification")
    private String certification;

}
