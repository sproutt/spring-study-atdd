package codesquad.web;

import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void createForm() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void create() throws Exception {
        String userId = "testuser";
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                               .addParameter("userId", userId)
                                                                               .addParameter("password", "password")
                                                                               .addParameter("name", "자바지기")
                                                                               .addParameter("email", "javajigi@slipp.net")
                                                                               .build();

        ResponseEntity<String> response = template().postForEntity("/questions", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findByUserId(userId).isPresent()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/questions");
    }

    @Test
    public void readList() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
        assertThat(response.getBody()).contains(defaultUser().getEmail());
    }

    @Test
    public void updateForm_no_login() throws Exception {
        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d/form", defaultUser().getId()), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void updateForm_login() throws Exception {
        User loginUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loginUser).getForEntity(String.format("/questions/%d/form", loginUser.getId()), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(defaultUser().getEmail());
    }

    @Test
    public void update_no_login() throws Exception {
        ResponseEntity<String> response = update(template());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update() throws Exception {
        ResponseEntity<String> response = update(basicAuthTemplate());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/questions");
    }

    private ResponseEntity<String> update(TestRestTemplate template) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                               .addParameter("_method", "put")
                                                                               .addParameter("password", "test")
                                                                               .addParameter("name", "자바지기2")
                                                                               .addParameter("email", "javajigi@slipp.net")
                                                                               .build();

        return template.postForEntity(String.format("/questions/%d", defaultUser().getId()), request, String.class);
    }

    @Test
    public void delete() throws Exception {
        ResponseEntity<String> response = delete(basicAuthTemplate());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/questions");
    }

    @Test
    public void delete_no_login() throws Exception {
        ResponseEntity<String> response = update(template());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<String> delete(TestRestTemplate template) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                               .addParameter("_method", "put")
                                                                               .addParameter("password", "test")
                                                                               .addParameter("name", "자바지기2")
                                                                               .addParameter("email", "javajigi@slipp.net")
                                                                               .build();

        return template.postForEntity(String.format("/questions/%d", defaultUser().getId()), request, String.class);
    }
}
