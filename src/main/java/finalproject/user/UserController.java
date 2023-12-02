package finalproject.user;

import finalproject.authorization.AuthService;
import finalproject.user.payloads.UserUpdateInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @PutMapping("/me")
    public UserDetails updateProfile(@AuthenticationPrincipal User currentUser, @RequestBody UserUpdateInfoDTO body) {
        return authService.update(currentUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentUser) {
        userService.delete(currentUser.getId());
    }

    @PatchMapping("/upload/me")
    @ResponseStatus(HttpStatus.OK)
    public User updateProfilePicture(@RequestParam("avatar") MultipartFile body,
                                     @AuthenticationPrincipal User currentUser) throws IOException {
        return userService.uploadPicture(body, currentUser.getId());
    }
}
