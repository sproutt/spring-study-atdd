package codesquad.web;

import codesquad.domain.UserRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createLoginForm() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/users/login", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void login_success() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String userId = "testuser";
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userId", userId);
        params.add("password", "password");
        params.add("name", "자바지기");
        params.add("email", "javajigi@slipp.net");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users/login");
    }
}