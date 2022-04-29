package codesquad.web;

import codesquad.HtmlFormDataBuilder;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createLoginForm() {
        ResponseEntity<String> response = template().getForEntity("/users/login", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void login_success() {
        String userId = "javajigi";
        String password = "test";
        String name = "자바지기";
        String email = "javajigi@slipp.net";
        User user = new User(userId, password, name, email);

        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        htmlFormDataBuilder.addParameter("userId", userId);
        htmlFormDataBuilder.addParameter("password", password);
        htmlFormDataBuilder.addParameter("name", name);
        htmlFormDataBuilder.addParameter("email", email);

        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();

        ResponseEntity<String> response = basicAuthTemplate(user).postForEntity("/users/login", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users");
    }
}