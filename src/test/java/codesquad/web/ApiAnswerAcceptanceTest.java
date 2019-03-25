package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    private static final String ANSWER_API = "/answers";
    private static final String QUESTION_API = "/api/questions";
    private String questionLocation;
    private String answerLocation;
    private Question question;
    private Answer answer;

    private static final Long QUESTION_ID = 1L;
    private static final Long ANSWER_ID = 1L;

    @Before
    public void setUp() {
        question = defaultQuestion();
        answer = defaultAnswer();
        questionLocation = createResource(QUESTION_API, question, defaultUser());
        answerLocation = createResource(questionLocation + ANSWER_API, answer, defaultUser());
    }

    @Test
    public void create() {
        Answer createdAnswer = getResource(answerLocation, Answer.class, defaultUser());
        assertThat(createdAnswer).isNotNull();
    }

    @Test
    public void create_failed() {
        ResponseEntity<Void> response = template().postForEntity(questionLocation + ANSWER_API, answer, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    public void delete() {
        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(defaultUser()).exchange(answerLocation, HttpMethod.DELETE, createHttpEntity(null), Answer.class);

        assertThat(responseEntity.getBody().isDeleted()).isTrue();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void delete_failed() {

        ResponseEntity<Answer> responseEntity =
                template().exchange(answerLocation, HttpMethod.DELETE, createHttpEntity(null), Answer.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
