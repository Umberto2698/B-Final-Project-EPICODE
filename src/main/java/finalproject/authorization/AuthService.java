package finalproject.authorization;

import finalproject.confirmationToken.ConfirmationToken;
import finalproject.confirmationToken.ConfirmationTokenService;
import finalproject.email.EmailSender;
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
    private EmailSender emailSender;
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
        return this.buildConfirmationPage("http://localhost:5173/");
    }

    public User save(UserRegisterDTO body) {
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
        user.setAddress(", - , ");
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
        emailSender.send(body.email(), this.buildEmail(body.name(), "http://localhost:8080/auth/confirm?token=" + token));
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

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    private String buildConfirmationPage(String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c;display:flex;align-items:center;justify-content:center;flex-direction:column;height:100%\">\n" +
                "\n" +
                "<p style=\"margin-bottom: 10px;font-size:19px;line-height:25px;color:#0b0c0c\">Email successfully confirmed!</p><blockquote style=\"margin: 0;font-size:19px;line-height:25px\"><p style=\"margin: 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Go to web page...</a> </p></blockquote>\n" +
                "\n" +
                "</div>";
    }
}
