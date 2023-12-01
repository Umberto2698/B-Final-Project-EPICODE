package finalproject.user;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import finalproject.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;

    public User getById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void delete(long id) {
        User found = this.getById(id);
        userRepository.delete(found);
    }

    public User uploadPicture(MultipartFile file, long id) throws IOException {
        User found = this.getById(id);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatarUrl(url);
        return userRepository.save(found);
    }
}
