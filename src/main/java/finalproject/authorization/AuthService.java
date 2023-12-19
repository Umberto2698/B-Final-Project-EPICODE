package finalproject.authorization;

import finalproject.confirmationToken.ConfirmationToken;
import finalproject.confirmationToken.ConfirmationTokenService;
import finalproject.email.Gmail;
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

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private Gmail gmail;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUser(UserLoginDTO body) {
        User user = userService.findByEmail(body.email());
        if (user.getEnable()) {
            if (bcrypt.matches(body.password(), user.getPassword())) {
                return jwtTools.createToken(user);
            } else {
                throw new UnauthorizedException("Email or password invalid.");
            }
        } else {
            throw new UnauthorizedException("Please verify your email.");
        }
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
        Calendar date = Calendar.getInstance();
        if (confirmationToken.getConfirmationDate() != null) {
            throw new IllegalArgumentException("email already confirmed.");
        } else if (confirmationToken.getExpiresAt().before(date.getTime())) {
            throw new IllegalArgumentException(("token expired."));
        }
        confirmationToken.setConfirmationDate(date.getTime());
        User user = userService.findByEmail(confirmationToken.getUser().getEmail());
        user.setEnable(true);
        userRepository.save(user);
        return gmail.buildConfirmationPage("http://localhost:5173/signUp/logIn");
    }

    public User save(UserRegisterDTO body) throws Exception {
        userRepository.findByEmail(body.email()).ifPresent(found -> {
            if (found.getEnable()) {
                throw new BadRequestException("The email " + found.getEmail() + " is alredy used.");
            } else {
                userService.delete(found.getId());
            }
        });
        User user = new User();
        user.setEmail(body.email());
        user.setName(body.name());
        user.setSurname(body.surname());
        user.setAddress(",  - , ");
        user.setRegion(Region.valueOf(body.region()));
        user.setPassword(bcrypt.encode(body.password()));
        user.setAvatarUrl("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());
        User newUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        Calendar date = Calendar.getInstance();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                date.getTime(),
                new Date(date.getTimeInMillis() + (15 * 60 * 1000)),
                newUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        gmail.sendEmail("Registration confirmation", gmail.buildEmail(body.name(), "http://localhost:8080/auth/confirm?token=" + token));
        return newUser;
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
