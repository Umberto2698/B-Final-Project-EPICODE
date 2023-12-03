package finalproject.authorization;

import finalproject.exceptions.BadRequestException;
import finalproject.exceptions.UnauthorizedException;
import finalproject.security.JWTTools;
import finalproject.user.User;
import finalproject.user.UserRepository;
import finalproject.user.UserService;
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
        user.setPassword(bcrypt.encode(body.password()));

        return userRepository.save(user);
    }

    public User update(UUID id, UserUpdateInfoDTO body) {
        User found = userService.getById(id);
        if (!body.name().isEmpty()) {
            found.setName(body.name());
        }
        if (!body.surname().isEmpty()) {
            found.setSurname(body.surname());
        }
        if (!body.password().isEmpty()) {
            found.setPassword(bcrypt.encode(body.password()));
        }
        if (!body.address().isEmpty()) {
            found.setAddress(body.address());
        }
        if (body.birthday() != null) {
            found.setBirthday(body.birthday());
        }
        return userRepository.save(found);
    }
}
