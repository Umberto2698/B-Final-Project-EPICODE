package finalproject.runners;

import finalproject.donation_center.DonationCenterRepository;
import finalproject.donation_center.DonationCenterService;
import finalproject.email.Gmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AddDonationCenters implements CommandLineRunner {
    @Autowired
    private DonationCenterService donationCenterService;
    @Autowired
    private DonationCenterRepository donationCenterRepository;

    @Autowired
    private Gmail gmail;

    @Override
    public void run(String... args) throws Exception {
        String donationCenterCsvUrl = "src/main/java/finalproject/dataset/donation_centers.csv";
        if (donationCenterRepository.findAll().isEmpty()) {
            donationCenterService.readDonationCenterFileCsv(donationCenterCsvUrl);
        }


    }
}
