package codesquad.service;

import codesquad.UnAuthenticationException;
import codesquad.UnAuthorizedException;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.NoSuchElementException;

@Service("userService")
public class UserService {

    public static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    public User add(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(User loginUser, long id, User updatedUser) {
        User original = findById(loginUser, id);
        original.update(loginUser, updatedUser);
        return original;
    }

    public User findById(User loginUser, long id) {
        return userRepository.findById(id)
                             .filter(user -> user.equals(loginUser))
                             .orElseThrow(UnAuthorizedException::new);
    }

    public User findByUserId(String userId) throws NoSuchElementException {
        return userRepository.findByUserId(userId)
                             .orElseThrow(NoSuchElementException::new);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User login(String userId, String password) throws UnAuthenticationException {
        // TODO 로그인 기능 구현
        return userRepository.findByUserId(userId)
                             .filter(user -> user.getPassword()
                                                 .equals(password))
                             .orElseThrow(UnAuthenticationException::new);
    }
}
