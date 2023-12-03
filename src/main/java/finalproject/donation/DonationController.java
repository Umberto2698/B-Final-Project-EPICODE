package finalproject.donation;

import finalproject.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NotContextException;

@RestController
@RequestMapping("/donations")
public class DonationController {
    @Autowired
    private DonationService donationService;

    @GetMapping("/me")
    public Page<Donation> getCurrentProfileDonations(@AuthenticationPrincipal User currentUser, @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id") String orderBy) throws NotContextException {
        return donationService.findByUserId(currentUser.getId(), page, size, orderBy);
    }

    @GetMapping("/me")
    public Page<Donation> getCurrentProfileDonationsByYear(@AuthenticationPrincipal User currentUser, @RequestParam int year, @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String orderBy) throws NotContextException {
        return donationService.getBillsByUserIdAndYear(currentUser.getId(), year, page, size, orderBy);
    }
}
