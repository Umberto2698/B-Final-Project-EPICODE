package finalproject.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import finalproject.config.CloudinaryService;
import finalproject.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private CloudinaryService cloudinaryService;

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No User with this email."));
    }

    public void delete(UUID id) {
        User found = this.getById(id);
        if (!found.getAvatarUrl().equals("https://ui-avatars.com/api/?name=" + found.getName() + "+" + found.getSurname())) {
            cloudinaryService.deleteImageByUrl(found.getAvatarUrl());
        }
        userRepository.delete(found);
    }

    public User uploadPicture(MultipartFile file, UUID id) throws IOException {
        User found = this.getById(id);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        if (!found.getAvatarUrl().equals("https://ui-avatars.com/api/?name=" + found.getName() + "+" + found.getSurname())) {
            cloudinaryService.deleteImageByUrl(found.getAvatarUrl());
        }
        found.setAvatarUrl(url);
        return userRepository.save(found);
    }
}
