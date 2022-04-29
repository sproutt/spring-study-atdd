package codesquad.web;

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

    @Test
    public void loginForm() {
        String expected = LOGIN_FORM_URI;
        when(userController.loginForm()).thenReturn(LOGIN_FORM_URI);

        String actual = userController.loginForm();

        assertThat(actual).isEqualTo(expected);
    }
}
