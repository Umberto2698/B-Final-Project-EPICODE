package finalproject.authorization;

import finalproject.exceptions.BadRequestException;
import finalproject.exceptions.UnauthorizedException;
import finalproject.security.JWTTools;
import finalproject.user.User;
import finalproject.user.UserRepository;
import finalproject.user.UserService;
import finalproject.user.enums.BloodType;
import finalproject.user.enums.Region;
import finalproject.user.enums.Sex;
import finalproject.user.payloads.UserLoginDTO;
import finalproject.user.payloads.UserRegisterDTO;
import finalproject.user.payloads.UserUpdateInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUser(UserLoginDTO body) {
        User user = userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Email or password invalid.");
        }
    }

    public User save(UserRegisterDTO body) {
        userRepository.findByEmail(body.email()).ifPresent(a -> {
            throw new BadRequestException("The email " + a.getEmail() + " is alredy used.");
        });
        User user = new User();
        user.setEmail(body.email());
        user.setName(body.name());
        user.setSurname(body.surname());
        user.setRegion(Region.valueOf(body.region()));
        user.setPassword(bcrypt.encode(body.password()));

        return userRepository.save(user);
    }

    public User update(UUID id, UserUpdateInfoDTO body) {
        User found = userService.getById(id);
        if (body.name() != null) {
            found.setName(body.name());
        }
        if (body.surname() != null) {
            found.setSurname(body.surname());
        }
        if (body.password() != null) {
            found.setPassword(bcrypt.encode(body.password()));
        }
        if (body.address() != null) {
            found.setAddress(body.address());
        }
        if (body.phone() != null) {
            found.setPhone(body.phone());
        }
        if (!body.bloodType().equals(found.getBloodtype().toString())) {
            found.setBloodtype(BloodType.valueOf(body.bloodType()));
        }
        if (!body.sex().equals(found.getSex().toString())) {
            found.setSex(Sex.valueOf(body.sex()));
        }
        if (body.birthday() != null) {
            found.setBirthday(body.birthday());
        }
        if (!body.region().equals(found.getRegion().toString())) {
            found.setRegion(Region.valueOf(body.region()));
        }
        if (1.50 >= body.height() && body.height() <= 2.80) {
            found.setHeight(body.height());
        }
        if (55 >= body.weight() && body.weight() <= 130) {
            found.setWeight(body.weight());
        }
        return userRepository.save(found);
    }
}
