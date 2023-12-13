package finalproject.donation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DonationRepository extends JpaRepository<Donation, UUID> {
    Page<Donation> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT d FROM Donation d WHERE EXTRACT(YEAR FROM d.donationDate)= :year AND d.user.id= :userId")
    Page<Donation> findByDonationDateYear(@Param("year") int year, @Param("userId") UUID userId, Pageable pageable);
}
