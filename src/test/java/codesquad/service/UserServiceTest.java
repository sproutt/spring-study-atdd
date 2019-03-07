package codesquad.service;

import codesquad.UnAuthenticationException;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import codesquad.web.dto.LoginUserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void login_success() throws Exception {
        //when
        User user = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        when(userRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(user));

        //then
        User loginUser = userService.login(new LoginUserDTO().toDTO(user));
        assertThat(loginUser).isEqualTo(user);
    }

    @Test(expected = UnAuthenticationException.class)
    public void login_failed_when_user_not_found() throws Exception {
        //when
        User failUser = new User("sanjigi222", "password", "name", "javajigi@slipp.net");
        when(userRepository.findByUserId(failUser.getUserId())).thenReturn(Optional.empty());

        //then
        userService.login(new LoginUserDTO().toDTO(failUser));
    }

    @Test(expected = UnAuthenticationException.class)
    public void login_failed_when_mismatch_password() throws Exception {
        //when
        User loginUser = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        when(userRepository.findByUserId(loginUser.getUserId())).thenReturn(Optional.of(loginUser));

        //then
        User failUser = new User("sanjigi", "password222", "name", "javajigi@slipp.net");
        userService.login(new LoginUserDTO().toDTO(failUser));
    }
}
