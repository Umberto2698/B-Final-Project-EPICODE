package finalproject.donation;

import finalproject.donation.enums.Check;
import finalproject.donation.payloads.DonationUpdateDTO;
import finalproject.donation.payloads.NewDonationDTO;
import finalproject.donation_center.DonationCenterService;
import finalproject.exceptions.NotFoundException;
import finalproject.user.User;
import finalproject.user.UserService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private DonationCenterService donationCenterService;

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
        return donationRepository.findByUserIdAndDonationDate(userId, year, pageable);
    }

    public Donation save(NewDonationDTO body, User user, UUID centerId) {
        Donation donation = new Donation();
        donation.setDonationDate(body.donationDate());
        donation.setUser(user);
        donation.setCenter(donationCenterService.getById(centerId));
        return donationRepository.save(donation);
    }

    public Donation getById(UUID id) {
        return donationRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Donation update(DonationUpdateDTO body, UUID id) {
        Donation found = this.getById(id);
        if (body.donationDate() != found.getDonationDate() && body.donationDate() != null) {
            found.setDonationDate(body.donationDate());
        }
        if (!body.check().equals(found.getCheck().toString())) {
            found.setCheck(Check.valueOf(body.check()));
        }
        return donationRepository.save(found);
    }
}
