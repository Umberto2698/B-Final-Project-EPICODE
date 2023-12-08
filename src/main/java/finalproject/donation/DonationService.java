package finalproject.donation;

import finalproject.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.naming.NotContextException;
import java.util.UUID;

@Service
public class DonationService {
    @Autowired
    private DonationRepository donationRepository;

    public Page<Donation> findByUserId(UUID userId, int page, int size, String orderBy) throws NotContextException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        if (pageable.getPageSize() <= 0 || pageable.isUnpaged()) {
            throw new NotContextException();
        }
        return donationRepository.findByUserId(userId, pageable);
    }

    public Page<Donation> getBillsByUserIdAndYear(UUID userId, int year, int page, int size, String orderBy) throws NotContextException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        if (pageable.getPageSize() <= 0 || pageable.isUnpaged()) {
            throw new NotContextException();
        }
        return donationRepository.findByDateYear(userId, year, pageable).orElseThrow(() -> new NotFoundException("No Record."));
    }
}
