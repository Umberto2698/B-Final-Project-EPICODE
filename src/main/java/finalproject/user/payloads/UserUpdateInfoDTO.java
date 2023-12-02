package finalproject.user.payloads;

import finalproject.user.enums.BloodType;
import finalproject.user.enums.Sex;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserUpdateInfoDTO(String name,
                                String surname,
                                @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Insert a valid email.")
                                String email,
                                String password,
                                String phone,
                                String address,
                                double height,
                                double weight,
                                BloodType bloodType,
                                String avatar,
                                LocalDate birthday,
                                Sex sex) {
}
