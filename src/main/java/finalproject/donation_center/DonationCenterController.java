package finalproject.donation_center;

import finalproject.user.enums.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("centers")
public class DonationCenterController {
    @Autowired
    private DonationCenterService donationCenterService;

    @GetMapping("/{id}")
    public DonationCenter getCenterById(@PathVariable UUID id) {
        return donationCenterService.getById(id);
    }

    @GetMapping("/region/{region}")
    public List<DonationCenter> getCenterListFromRegion(@PathVariable Region region) {
        return donationCenterService.getCenterFromRegion(region);
    }
}
