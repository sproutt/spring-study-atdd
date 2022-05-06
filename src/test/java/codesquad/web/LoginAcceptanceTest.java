package codesquad.web;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class LoginAcceptanceTest extends AcceptanceTest {

    private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

    @Test
    public void login_success() throws Exception {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userId", defaultUser().getUserId());
        params.add("password", defaultUser().getPassword());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

        log.debug("request = {}", request);

        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/users/login", request, String.class);

        //then
        assertAll(
                () -> assertEquals(response.getStatusCode(), HttpStatus.FOUND),
                () -> assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users")
        );
    }

    @Test
    public void login_fail() throws Exception{
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("userId", defaultUser().getUserId());
        params.add("password", defaultUser().getPassword() + "1");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

        log.debug("request = {}", request);

        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/users/login", request, String.class);

        log.info("response = {}", response);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
