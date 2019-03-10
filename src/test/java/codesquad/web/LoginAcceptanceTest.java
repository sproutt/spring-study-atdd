package codesquad.web;

import codesquad.domain.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AcceptanceTest {


    @Autowired
    UserRepository userRepository;

    @Test
    public void loginForm() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/users/login", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void login() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String userId = "testuser";
        String password = "test";

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userId", userId);
        params.add("password", password);
        params.add("name", "bellroute");
        params.add("email", "bell@gmail.com");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

        ResponseEntity<String> response = template().postForEntity("/users", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
        assertThat(userRepository.findByUserId(userId).orElseThrow(RuntimeException::new).matchPassword(password));
        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users");
    }
}
