package codesquad.web;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.HtmlFormDataBuilder;
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

public class LoginAcceptanceTest extends AcceptanceTest {

    private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

    @Autowired
    private UserRepository userRepository;

    @Test
    public void login() throws Exception {

        String userId = "javajigi";
        String userPassword = "test";

        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                .addParameter("userId", userId)
                                                                .addParameter("password", userPassword)
                                                                .build();

        ResponseEntity<String> response = template()
                                              .postForEntity("/users/login", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
        assertThat(userRepository.findByUserId(userId).orElseThrow(IllegalAccessError::new)
                       .checkPassword(userPassword)).isTrue();

        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/");
    }

}
