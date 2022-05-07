package codesquad.web;

import codesquad.HtmlFormDataBuilder;
import codesquad.domain.User;
import codesquad.security.HttpSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.*;

public class LoginAcceptanceTest extends AcceptanceTest {
    public static final Logger log =  LoggerFactory.getLogger(LoginAcceptanceTest.class);

    @Test
    public void login_success() throws Exception {
        //given
        String userId = "user1";
        String password = "password1";
        String name = "name1";
        String email = "email1@gmail.com";
        User user = new User(userId, password, name, email);
        save(user);

        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        htmlFormDataBuilder.addParameter("userId", userId);
        htmlFormDataBuilder.addParameter("password", password);
        htmlFormDataBuilder.addParameter("name", name);
        htmlFormDataBuilder.addParameter("email", email);
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        MockHttpSession session = new MockHttpSession();

        // when
        ResponseEntity<String> response = basicAuthTemplate(user).postForEntity("/users/login", request, String.class);
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, findByUserId(userId));

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(session.getAttribute(HttpSessionUtils.USER_SESSION_KEY)).isEqualTo(user);
    }

    @Test
    public void login_failed() throws Exception {
        // given
        String userId = "user1";
        String password = "password1";
        String name = "name1";
        String email = "email1@gmail.com";
        User user = new User(userId, password, name, email);
        save(user);

        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        htmlFormDataBuilder.addParameter("userId", "user2");
        htmlFormDataBuilder.addParameter("password", password);
        htmlFormDataBuilder.addParameter("name", name);
        htmlFormDataBuilder.addParameter("email", email);
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        MockHttpSession session = new MockHttpSession();

        // when
        ResponseEntity<String> response = basicAuthTemplate(user).postForEntity("/users/login", request, String.class);
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
