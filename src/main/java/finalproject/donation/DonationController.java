package finalproject.donation;

import finalproject.donation.payloads.DonationUpdateDTO;
import finalproject.donation.payloads.NewDonationDTO;
import finalproject.exceptions.BadRequestException;
import finalproject.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.NotContextException;
import java.util.UUID;

@RestController
@RequestMapping("/donations")
public class DonationController {
    @Autowired
    private DonationService donationService;

    @GetMapping("/me")
    public Page<Donation> getCurrentProfileDonations(@AuthenticationPrincipal User currentUser, @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "donationDate") String orderBy) throws NotContextException {
        return donationService.findByUserId(currentUser.getId(), page, size, orderBy);
    }

    @GetMapping("/me/{year}")
    public Page<Donation> getCurrentProfileDonationsByYear(@AuthenticationPrincipal User currentUser, @PathVariable int year, @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "donationDate") String orderBy) throws NotContextException {
        return donationService.getBillsByUserIdAndYear(currentUser.getId(), year, page, size, orderBy);
    }

    @PostMapping("/me/{id}")
    public Donation save(@AuthenticationPrincipal User currentUser, @PathVariable UUID id, @RequestBody @Validated NewDonationDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("", validation.getAllErrors());
        } else {
            return donationService.save(body, currentUser, id);
        }
    }

    @PutMapping("/{id}")
    public Donation update(@PathVariable UUID id, @RequestBody @Validated DonationUpdateDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("", validation.getAllErrors());
        } else {
            return donationService.update(body, id);
        }
    }
}
