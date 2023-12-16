package finalproject.donation;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.donation.enums.Check;
import finalproject.donation_center.DonationCenter;
import finalproject.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "donation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")

    private LocalDate donationDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "check_in")
    private Check check = Check.NO;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private DonationCenter center;
}
