package finalproject.user;

import finalproject.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

}
