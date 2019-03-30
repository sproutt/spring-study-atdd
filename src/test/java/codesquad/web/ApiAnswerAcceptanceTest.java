package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Answer;
import org.junit.Test;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    @Test
    public void show() {
        ResponseEntity<Answer> response = basicAuthTemplate().postForEntity(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer(), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = String.format("/api/questions/%d", defaultQuestion().getId());

        response = basicAuthTemplate().getForEntity(location, Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void create() {
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer(), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();
        Answer dbAnswer = basicAuthTemplate().getForObject(location, Answer.class);
        assertThat(dbAnswer).isNotNull();
    }

    @Test
    public void update() {
        ResponseEntity<Answer> response = basicAuthTemplate().postForEntity(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer(), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer updateAnswer = new Answer(defaultUser(), "content");
        response = basicAuthTemplate()
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateAnswer), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateAnswer.equalsContents(response.getBody())).isTrue();
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }

    @Test
    public void update_another_user() {
        ResponseEntity<Answer> response = basicAuthTemplate().postForEntity(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer(), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer updateAnswer = new Answer(defaultUser(), "content");
        response = basicAuthTemplate(newUser("testuser1"))
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateAnswer), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() throws CannotDeleteException {
        ResponseEntity<Answer> response = basicAuthTemplate().postForEntity(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer(), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer deleteAnswer = defaultAnswer();
        deleteAnswer.delete(defaultUser());

        response = basicAuthTemplate()
                .exchange(location, HttpMethod.DELETE, createHttpEntity(deleteAnswer), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void delete_another_user() throws CannotDeleteException {
        ResponseEntity<Answer> response = basicAuthTemplate().postForEntity(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer(), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer deleteAnswer = defaultAnswer();
        deleteAnswer.delete(defaultUser());

        response = basicAuthTemplate(newUser("testuser1"))
                .exchange(location, HttpMethod.DELETE, createHttpEntity(deleteAnswer), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
