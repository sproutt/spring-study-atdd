package codesquad.web;

import codesquad.HtmlFormDataBuilder;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.*;

@Transactional
public class LoginAcceptanceTest extends AcceptanceTest {
    public static final Logger log =  LoggerFactory.getLogger(LoginAcceptanceTest.class);

    private static String userId;
    private static String password;
    private static String name;
    private static String email;

    @BeforeClass
    public static void init() {
        userId = "javajigi";
        password = "test";
        name = "자바지기";
        email = "javajigi@slipp.net";
    }

    @Test
    public void login_success() throws Exception {
        //given
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        htmlFormDataBuilder.addParameter("userId", userId);
        htmlFormDataBuilder.addParameter("password", password);
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();;

        // when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/users/login", request, String.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders()
                           .get("Set-Cookie")
                           .get(0)).isNotEmpty();
    }

    @Test
    public void login_failed() throws Exception {
        // given
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        htmlFormDataBuilder.addParameter("userId", "wrongId");
        htmlFormDataBuilder.addParameter("password", password);
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();

        // when
        ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);
        log.debug("response의 헤더 = {}", response.getHeaders());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders()
                           .get("Set-Cookie")).isNull();
    }
}
