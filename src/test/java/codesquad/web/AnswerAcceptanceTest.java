package codesquad.web;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.HtmlFormDataBuilder;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

public class AnswerAcceptanceTest extends AcceptanceTest {

    @Test
    public void create_login() throws Exception {
        User loginUser = defaultUser();
        ResponseEntity<String> response = create(basicAuthTemplate(loginUser));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(defaultQuestion().getAnswers().size()).isNotZero();
    }

    @Test
    public void create_no_login() throws Exception {
        ResponseEntity<String> response = create(template());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(defaultQuestion().getAnswers().size()).isNotZero();
    }

    private ResponseEntity<String> create(TestRestTemplate template) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                .addParameter("contents", "test_contents")
                                                                .build();
        return template.postForEntity(String.format("/questions/%d/answers/form", defaultQuestion().getId()), request,
                       String.class);
    }

    @Test
    public void delete_no_login() throws Exception {
        ResponseEntity<String> response = delete(template());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_login() throws Exception {
        ResponseEntity<String> response = delete(template());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }

    private ResponseEntity<String> delete(TestRestTemplate template) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                .addParameter("_method", "delete")
                                                                .build();
        return template.postForEntity(String.format("/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId()), request, String.class);
    }
}
