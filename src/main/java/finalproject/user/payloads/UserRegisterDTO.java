package finalproject.user.payloads;

import finalproject.user.enums.Region;
import finalproject.user.validators.ValidRegion;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRegisterDTO(@NotEmpty(message = "The name is required.")
                              String name,
                              @NotEmpty(message = "The surname is required.")
                              String surname,
                              @NotNull(message = "The region is required.")
                              @ValidRegion(enumClass = Region.class,
                                      message = "Region not valid")
                              String region,
                              @NotEmpty(message = "The email is required.")
                              @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Insert a valid email.")
                              String email,
                              @NotEmpty(message = "The password is required.")
                              String password
) {
}
