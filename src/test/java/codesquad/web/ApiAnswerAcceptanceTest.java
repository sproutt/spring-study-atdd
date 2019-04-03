package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private Logger log = LoggerFactory.getLogger(ApiAnswerAcceptanceTest.class);
    private User anotherUser = newUser("testuser1");

    @Test
    public void show() {
        ResponseEntity<Answer> response = createAnswerResource(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer());
        String location = String.format("/api/questions/%d", defaultQuestion().getId());

        response = basicAuthTemplate().getForEntity(location, Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void create() {
        ResponseEntity<Answer> response = createAnswerResource(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer());
        String location = response.getHeaders().getLocation().getPath();

        Answer dbAnswer = getResource(location, Answer.class, defaultUser());
        assertThat(dbAnswer).isNotNull();
    }

    @Test
    public void update() {
        ResponseEntity<Answer> response = createAnswerResource(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer());
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer updateAnswer = defaultAnswer().setContents("this is update");
        response = basicAuthTemplate()
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateAnswer), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateAnswer.equals(response.getBody())).isTrue();
    }

    @Test
    public void update_another_user() {
        ResponseEntity<Answer> response = createAnswerResource(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer());
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer updateAnswer = defaultAnswer().setContents("this is update");
        response = basicAuthTemplate(anotherUser)
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateAnswer), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() {
        ResponseEntity<Answer> response = createAnswerResource(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer());
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer deleteAnswer = defaultAnswer();
        deleteAnswer.delete(defaultUser());

        response = basicAuthTemplate()
                .exchange(location, HttpMethod.DELETE, createHttpEntity(deleteAnswer), Answer.class);
        log.info("response.body : ",response.getBody());
        log.info("response.status :", response.getStatusCode());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void delete_another_user() {
        ResponseEntity<Answer> response = createAnswerResource(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer());
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer deleteAnswer = defaultAnswer();
        deleteAnswer.delete(defaultUser());

        response = basicAuthTemplate(anotherUser)
                .exchange(location, HttpMethod.DELETE, createHttpEntity(deleteAnswer), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_no_login() {
        ResponseEntity<Answer> response = createAnswerResource(String.format("/api/questions/%d/answers", defaultQuestion().getId()), defaultAnswer());
        String location = String.format("/api/questions/%d/answers/%d", defaultQuestion().getId(), defaultAnswer().getId());

        Answer deleteAnswer = defaultAnswer();
        deleteAnswer.delete(defaultUser());

        response = template()
                .exchange(location, HttpMethod.DELETE, createHttpEntity(deleteAnswer), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
