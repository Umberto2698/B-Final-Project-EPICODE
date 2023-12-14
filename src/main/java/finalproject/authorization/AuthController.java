package finalproject.authorization;

import finalproject.exceptions.BadRequestException;
import finalproject.user.User;
import finalproject.user.payloads.UserLoginDTO;
import finalproject.user.payloads.UserRegisterDTO;
import finalproject.user.payloads.UserSuccessLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserSuccessLoginDTO login(@RequestBody UserLoginDTO body) throws Exception {
        return new UserSuccessLoginDTO(authService.authenticateUser(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser(@RequestBody @Validated UserRegisterDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException("", validation.getAllErrors());
        } else {
            return authService.save(body);
        }
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return authService.confirmToken(token);
    }
}
