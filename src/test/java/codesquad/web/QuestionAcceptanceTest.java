package codesquad.web;

import codesquad.domain.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

    @Test
    public void createForm() {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).getForEntity("/questions/form", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void create_login() {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                               .addParameter("title", "헬로우 베이비들")
                                                                               .addParameter("contents", "what the puck")
                                                                               .addParameter("deleted", "false")
                                                                               .build();

        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).postForEntity("/questions", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders()
                           .getLocation()
                           .getPath()).startsWith("/questions");
    }

    @Test
    public void create_no_login() {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                               .addParameter("title", "헬로우 베이비들")
                                                                               .addParameter("contents", "what the puck")
                                                                               .addParameter("deleted", "false")
                                                                               .build();

        ResponseEntity<String> response = template().postForEntity("/questions", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void show() {
        ResponseEntity<String> response = template().getForEntity("/questions/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void readList() {
        ResponseEntity<String> response = template().getForEntity("/questions", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
        assertThat(response.getBody()).contains("qna-list");
    }

    @Test
    public void updateForm_no_login() {
        ResponseEntity<String> response = template().getForEntity("/questions/1/form", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void updateForm_login() {
        User loginUser = defaultUser();

        ResponseEntity<String> response = basicAuthTemplate(loginUser).getForEntity("/questions/1/form", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update_no_login() {
        ResponseEntity<String> response = update(template());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update_login() {
        ResponseEntity<String> response = update(basicAuthTemplate());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders()
                           .getLocation()
                           .getPath()).startsWith("/questions");
    }

    private ResponseEntity<String> update(TestRestTemplate template) {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                               .addParameter("_method", "put")
                                                                               .addParameter("title", "헬로우 베이비들2")
                                                                               .addParameter("contents", "what the puck2")
                                                                               .addParameter("deleted", "false")
                                                                               .build();

        return template.postForEntity("/questions/1", request, String.class);
    }

    @Test
    public void delete_login() {
        ResponseEntity<String> response = delete(basicAuthTemplate(defaultUser()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders()
                           .getLocation()
                           .getPath()).startsWith("/questions");
    }

    @Test
    public void delete_no_login() {
        ResponseEntity<String> response = delete(template());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<String> delete(TestRestTemplate template) {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                               .addParameter("_method", "delete")
                                                                               .build();

        return template.postForEntity("/questions/1", request, String.class);
    }
}
