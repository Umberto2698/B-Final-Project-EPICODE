package finalproject.donation;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonationRepository extends JpaRepository<Donation, UUID> {
    Page<Donation> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT d FROM Donation d WHERE d.user.id = :userId AND EXTRACT(YEAR FROM d.donationDate)= :year")
    Optional<Page<Donation>> findByDateYear(@Param("userId") UUID userId, @Param("year") int year, Pageable pageable);
}
