package codesquad.web;

import codesquad.domain.Question;
import org.junit.Test;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void create() {
        ResponseEntity<Question> response = template().postForEntity(String.format("/api/questions/%d", defaultQuestion().getId()), defaultQuestion(), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();
        Question dbQuestion = basicAuthTemplate().getForObject(location, Question.class);
        assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void show() {
        ResponseEntity<Question> response = template().postForEntity(String.format("/api/questions/%d", defaultQuestion().getId()), defaultQuestion(), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();
        response = basicAuthTemplate().getForEntity(location, Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update() {
        ResponseEntity<Question> response = template().postForEntity(String.format("/api/questions/%d", defaultQuestion().getId()), defaultQuestion(), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();
        Question updateQuestion = new Question("title", "contents");
        response = basicAuthTemplate()
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateQuestion.equals(response.getBody())).isTrue();
    }

    @Test
    public void update_another_user() {
        ResponseEntity<Question> response = template().postForEntity(String.format("/api/questions/%d", defaultQuestion().getId()), defaultQuestion(), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();
        Question updateQuestion = new Question("title", "contents");
        response = basicAuthTemplate(newUser("testuser1"))
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}
