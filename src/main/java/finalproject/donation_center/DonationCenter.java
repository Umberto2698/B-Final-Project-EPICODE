package finalproject.donation_center;

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
    private String cap;
    @Enumerated(EnumType.STRING)
    private Region region;
    @OneToMany(mappedBy = "center")
    private List<Donation> donations;
}
