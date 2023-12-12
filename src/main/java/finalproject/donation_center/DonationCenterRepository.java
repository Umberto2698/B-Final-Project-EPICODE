package finalproject.donation_center;

import finalproject.user.enums.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DonationCenterRepository extends JpaRepository<DonationCenter, UUID> {
    List<DonationCenter> findByRegion(Region region);
}
