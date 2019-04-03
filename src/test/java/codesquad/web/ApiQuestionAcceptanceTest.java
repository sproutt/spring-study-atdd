package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final String URL = "/api/questions";
    private Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);

    @Test
    public void create() {
        ResponseEntity<Question> response = createQuestionResource(URL, defaultQuestion());
        String location = response.getHeaders().getLocation().getPath();
        Question dbQuestion = getResource(location, Question.class, defaultUser());
        assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void show() {
        ResponseEntity<Question> response = createQuestionResource(URL, defaultQuestion());
        String location = response.getHeaders().getLocation().getPath();
        response = basicAuthTemplate().getForEntity(location, Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update() {
        ResponseEntity<Question> response = createQuestionResource(URL, defaultQuestion());
        String location = response.getHeaders().getLocation().getPath();

        Question updateQuestion = new Question("title", "contents");
        response = basicAuthTemplate()
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateQuestion.isEqualsTitleAndContents(response.getBody())).isTrue();
    }

    @Test
    public void update_another_user() {
        ResponseEntity<Question> response = createQuestionResource(URL, defaultQuestion());
        String location = response.getHeaders().getLocation().getPath();

        Question updateQuestion = new Question("title", "contents");
        response = basicAuthTemplate(newUser("testuser1"))
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update_no_login() {
        ResponseEntity<Question> response = createQuestionResource(URL, defaultQuestion());
        String location = response.getHeaders().getLocation().getPath();

        Question updateQuestion = new Question("title", "contents");
        response = template()
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() throws CannotDeleteException {
        ResponseEntity<Question> response = createQuestionResource(URL, defaultQuestion());
        String location = response.getHeaders().getLocation().getPath();

        Question deletedQuestion = defaultQuestion();
        deletedQuestion.delete(defaultUser());

        response = basicAuthTemplate()
                .exchange(location, HttpMethod.DELETE, createHttpEntity(deletedQuestion), Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void delete_another_user() throws CannotDeleteException {
        ResponseEntity<Question> response = createQuestionResource(URL, defaultQuestion());
        String location = response.getHeaders().getLocation().getPath();

        Question deletedQuestion = defaultQuestion();
        deletedQuestion.delete(defaultUser());

        response = basicAuthTemplate(newUser("testuser1"))
                .exchange(location, HttpMethod.DELETE, createHttpEntity(deletedQuestion), Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_no_login() throws CannotDeleteException {
        ResponseEntity<Question> response = createQuestionResource(URL, defaultQuestion());
        String location = response.getHeaders().getLocation().getPath();

        Question deletedQuestion = defaultQuestion();
        deletedQuestion.delete(defaultUser());

        response = template()
                .exchange(location, HttpMethod.DELETE, createHttpEntity(deletedQuestion), Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
