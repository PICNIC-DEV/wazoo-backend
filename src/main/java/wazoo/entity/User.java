package wazoo.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "native_language")
    private String nativeLanguage;

    @Column(name = "travel_type")
    private String travelType;

    @Column(name = "user_temperature")
    private int userTemperature;

    @Column(name = "address")
    private String address;

    @Column(name = "preferred_country")
    private String preferredCountry;

    @Column(name = "user_state")
    private String userState;

    @Column(name = "coin")
    private int coin;
}