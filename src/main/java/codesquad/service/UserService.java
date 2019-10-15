package codesquad.service;

import codesquad.domain.User;
import codesquad.domain.UserRepository;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User add(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(User loginUser, long id, User updatedUser) throws Exception {
        User originalUser = findById(loginUser, id);

        return originalUser.update(loginUser, updatedUser);
    }

    public User findById(User loginUser, long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return user.checkSameUser(loginUser);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User login(String userId, String password) throws Exception {
        User user = userRepository.findByUserId(userId).orElseThrow(EntityNotFoundException::new);

        return user.checkPassword(password);
    }
}
