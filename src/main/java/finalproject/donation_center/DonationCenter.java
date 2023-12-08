package finalproject.donation_center;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
