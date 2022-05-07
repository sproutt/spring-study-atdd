package codesquad.web;

import codesquad.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private static final String LOGIN_FORM_URI = "/user/login";

    @Mock
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Test
    public void loginForm() {
        String expected = LOGIN_FORM_URI;
        when(userController.loginForm()).thenReturn(LOGIN_FORM_URI);

        String actual = userController.loginForm();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void login() {
        // 로그인 로직은 서비스 레이어에 위임
        // 컨트롤러에서 무엇을 테스트할 것인가?
        // 컨트롤러에서는 login 메서드에 대한 테스트 커버리지만 달성하면 된다.
        // 무엇을 테스트할 것인가?
        // 컨트롤러에서는 로그인 로직을 서비스 레이어에 위임한 채
        // 로그인 성공 시 "redirect:/users"를
        // 로그인 실패 시 "template/user 디렉토리의 login_failed.html을 응답"
    }
}
