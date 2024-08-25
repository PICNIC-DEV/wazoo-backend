package wazoo.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wazoo.RoleType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Integer userNo;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name ="user_password", nullable = false)
    private String userPassword;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "coin")
    private Integer coin;

    @Column(name = "user_state", nullable = false)
    private String userState;

    @Column(name = "travel_type")
    private String travelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private RoleType role;
}