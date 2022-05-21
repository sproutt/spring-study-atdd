package codesquad.service;

import codesquad.UnAuthenticationException;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void login_success() throws Exception {
        User user = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

        User loginUser = userService.login(user.getUserId(), user.getPassword());
        assertThat(loginUser).isEqualTo(user);
    }

    @Test
    public void login_failed_when_user_not_found() throws Exception {
        when(userRepository.findByUserId("sanjigi")).thenReturn(Optional.empty());


        assertThrows(
                UnAuthenticationException.class, () -> userService.login("sanjigi", "password")
        );
    }

    @Test
    public void login_failed_when_mismatch_password() throws Exception {
        User user = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

        assertThrows(
                UnAuthenticationException.class, () -> userService.login(user.getUserId(), user.getPassword() + "2")
        );
    }
}
