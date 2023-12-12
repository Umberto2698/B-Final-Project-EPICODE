package finalproject.donation_center.payloads;

import finalproject.user.enums.Region;
import finalproject.user.validators.ValidRegion;
import jakarta.validation.constraints.NotNull;

public record RegionDTO(@NotNull(message = "The region is required.")
                        @ValidRegion(enumClass = Region.class,
                                message = "Region not valid")
                        String region) {
}
