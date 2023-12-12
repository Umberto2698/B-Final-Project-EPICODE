package finalproject.donation.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewDonationDTO(
        @NotNull(message = "donation date is required.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate donationDate) {
}
