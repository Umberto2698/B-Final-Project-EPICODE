package finalproject.donation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DonationRepository extends JpaRepository<Donation, UUID> {
    Page<Donation> findByUserId(UUID userId, Pageable pageable);

    Page<Donation> findByUserIdAndDonationDate(@Param("userId") UUID userId, @Param("year") int year, Pageable pageable);
}
