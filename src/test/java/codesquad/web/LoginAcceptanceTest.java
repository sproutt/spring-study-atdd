package codesquad.web;

import codesquad.domain.UserRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static org.assertj.core.api.Assertions.assertThat;


public class LoginAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void loginForm() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/users/login", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void login_sucess() throws Exception {
        String userId = "javajigi";
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                                .addParameter("userId", userId)
                                                                                .addParameter("password", "test")
                                                                                .build();

        ResponseEntity<String> response = template.postForEntity("/users/login", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/users");
    }

    @Test
    public void login_fail() throws Exception {
        String userId = "javajigi1";
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                                .addParameter("userId", userId)
                                                                                .addParameter("password", "test")
                                                                                .build();

        ResponseEntity<String> response = template.postForEntity("/users/login", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
