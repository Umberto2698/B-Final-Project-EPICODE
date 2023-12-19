package finalproject.donation_center;

import com.fasterxml.jackson.annotation.JsonIgnore;
import finalproject.donation.Donation;
import finalproject.user.enums.Region;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donationCenters")
public class DonationCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String address;
    private String municipality;
    @Column(name = "province_abbreviation")
    private String provinceAbbreviation;
    private String cap;
    private String denomination;
    @Enumerated(EnumType.STRING)
    private Region region;
    @OneToMany(mappedBy = "center")
    @JsonIgnore
    private List<Donation> donations;
}
