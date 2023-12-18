package finalproject.donation.payloads;

import finalproject.donation.enums.Check;
import finalproject.donation.validators.ValidCheck;

import java.time.LocalDate;

public record DonationUpdateDTO(

        LocalDate donationDate,
        @ValidCheck(enumClass = Check.class,
                message = "Check state not valid")
        String check) {
}
