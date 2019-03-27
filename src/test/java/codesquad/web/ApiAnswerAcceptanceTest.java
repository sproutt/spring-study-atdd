package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.UserTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private static final String QUESTION_URI = "/api/questions";
    private static final String ANSWER_URI = "/answers";
    private static HtmlFormDataBuilder htmlFormDataBuilder;
    private Question createdQuestion;
    private Answer createdAnswer;

    @Before
    public void setUp() throws Exception {
        htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
        createdQuestion = new Question("title", "contents");
        createdAnswer = new Answer(defaultUser(), "contents");
        createdQuestion.addAnswer(createdAnswer);
        createdAnswer.toQuestion(createdQuestion);
    }

    @Test
    public void create() throws Exception {
        String location = createResource(QUESTION_URI, createdQuestion, defaultUser());
        createResource(location + ANSWER_URI, createdAnswer, defaultUser());
        Answer answer = basicAuthTemplate().getForObject(location, Answer.class);
        assertThat(answer).isNotNull();
    }

    @Test
    public void create_failed() {
        String location = createResource(QUESTION_URI, createdQuestion, defaultUser());
        ResponseEntity<Void> answerResponse = template().postForEntity(location + ANSWER_URI, createdAnswer, Void.class);
        assertThat(answerResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }



    @Test
    public void delete() {
        String location = createResource(QUESTION_URI, createdQuestion, defaultUser());
        String answerLocation = createResource(location + ANSWER_URI, createdAnswer, defaultUser());
        Answer answer = basicAuthTemplate().getForObject(answerLocation, Answer.class);
        ResponseEntity<Void> responseEntity =
                basicAuthTemplate().exchange(String.format("/api/questions/%d/answers/%d", 1l, 1l), HttpMethod.DELETE, createHttpEntity(null), Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void delete_no_login() {
        ResponseEntity<Void> responseEntity =
                template().exchange(String.format("/api/questions/%d/answers/%d", 1l, 1l), HttpMethod.DELETE, createHttpEntity(null), Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_different_user(){
        ResponseEntity<Void> responseEntity =
                basicAuthTemplate().exchange(String.format("/api/questions/%d/answers/%d", 1l, 2l), HttpMethod.DELETE, createHttpEntity(null), Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

}
