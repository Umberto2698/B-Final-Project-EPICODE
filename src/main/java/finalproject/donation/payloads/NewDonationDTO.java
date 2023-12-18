package finalproject.donation.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewDonationDTO(
        @NotNull(message = "donation date is required.")

        LocalDate donationDate) {
}
