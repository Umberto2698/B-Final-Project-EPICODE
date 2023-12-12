package finalproject.donation.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.donation.enums.Check;
import finalproject.donation.validators.ValidCheck;

import java.time.LocalDate;

public record DonationUpdateDTO(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate donationDate,
        @ValidCheck(enumClass = Check.class,
                message = "Check state not valid")
        String check) {
}
