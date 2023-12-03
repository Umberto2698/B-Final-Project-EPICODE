package finalproject.user.payloads;

import finalproject.user.enums.BloodType;
import finalproject.user.enums.Sex;

import java.time.LocalDate;

public record UserUpdateInfoDTO(String name,
                                String surname,
                                String password,
                                String phone,
                                String address,
                                double height,
                                double weight,
                                BloodType bloodType,
                                LocalDate birthday,
                                Sex sex) {
}
