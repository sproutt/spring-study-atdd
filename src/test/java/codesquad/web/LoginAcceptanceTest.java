package codesquad.web;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.helper.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginAcceptanceTest extends AcceptanceTest {

    private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

    @Test
    public void login_success() throws Exception {
        //given
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
        htmlFormDataBuilder.addParameter("userId", defaultUser().getUserId());
        htmlFormDataBuilder.addParameter("password", defaultUser().getPassword());

        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/users/login", htmlFormDataBuilder.build(), String.class);

        log.info("response = {}", response);

        //then
        assertAll(
                () -> assertEquals(response.getStatusCode(), HttpStatus.FOUND),
                () -> assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users")
        );
    }

    @Test
    public void login_fail() throws Exception {
        //given
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
        htmlFormDataBuilder.addParameter("userId", defaultUser().getUserId());
        htmlFormDataBuilder.addParameter("password", defaultUser().getPassword() + "1");

        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/users/login", htmlFormDataBuilder.build(), String.class);

        log.info("response = {}", response);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
