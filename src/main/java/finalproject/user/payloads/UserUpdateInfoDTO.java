package finalproject.user.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.user.enums.BloodType;
import finalproject.user.enums.Region;
import finalproject.user.enums.Sex;
import finalproject.user.validators.ValidBloodType;
import finalproject.user.validators.ValidRegion;
import finalproject.user.validators.ValidSex;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UserUpdateInfoDTO(String name,
                                String surname,
                                String password,
                                @Pattern(regexp = "^\\+?\\d{1,3}(\\s*\\d){7}$", message = "insert a valid phone number")
                                String phone,
                                String address,
                                @ValidRegion(enumClass = Region.class,
                                        message = "Region not valid")
                                String region,
                                double height,
                                double weight,
                                @ValidBloodType(enumClass = BloodType.class,
                                        message = "Blood type not valid")
                                String bloodType,
                                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
                                LocalDate birthday,
                                @ValidSex(enumClass = Sex.class,
                                        message = "Sex not valid")
                                String sex) {
}
