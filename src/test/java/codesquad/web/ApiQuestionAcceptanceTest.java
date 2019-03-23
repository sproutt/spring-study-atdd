package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final String QUESTION_API = "/api/questions";

    private Question newQuestion;
    private String resourceLocation;

    @Autowired
    private QuestionRepository questionRepository;

    @Before
    public void setUp() {
        newQuestion = new Question("newTitle", "newContents");
        newQuestion.writeBy(defaultUser());
        resourceLocation = createResource(QUESTION_API, newQuestion, defaultUser());


    }

    @Test
    public void create() {
        Question question = getResource(resourceLocation, Question.class, defaultUser());
        assertThat(question).isNotNull();
    }

    @Test
    public void create_no_login() {
        ResponseEntity<Void> response = template().postForEntity(QUESTION_API, newQuestion, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void show() {
        Question question = getResource(resourceLocation, Question.class, defaultUser());
        assertThat(question.getTitle()).isEqualTo(newQuestion.getTitle());
        assertThat(question.getContents()).isEqualTo(newQuestion.getContents());
    }

    @Test
    public void update() {
        ResponseEntity<Question> responseEntity =
                basicAuthTemplate().exchange(resourceLocation, HttpMethod.PUT, createHttpEntity(newQuestion), Question.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getTitle()).isEqualTo(newQuestion.getTitle());
        assertThat(responseEntity.getBody().getContents()).isEqualTo(newQuestion.getContents());
    }

    @Test
    public void update_failed() {
        ResponseEntity<Question> responseEntity =
                template().exchange(resourceLocation, HttpMethod.PUT, createHttpEntity(newQuestion), Question.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() {
        ResponseEntity<Question> responseEntity =
                basicAuthTemplate().exchange(resourceLocation, HttpMethod.DELETE, createHttpEntity(newQuestion), Question.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void delete_failed() {
        ResponseEntity<Question> responseEntity =
                template().exchange(resourceLocation, HttpMethod.DELETE, createHttpEntity(newQuestion), Question.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
