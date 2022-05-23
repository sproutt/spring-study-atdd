package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static codesquad.domain.QuestionTest.newQuestion;
import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final String DEFAULT_QUESTION_URL = "/api/questions";

    private Question newQuestion;

    @Before
    public void setUp() throws Exception {
        newQuestion = defaultQuestion();
    }

    @Test
    public void create() {
        Question newQuestion = defaultQuestion();
        String location = createResource(DEFAULT_QUESTION_URL, newQuestion);

        Question dbQuestion = getResource(location, Question.class);
        assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void show() {
        String location = createResource(DEFAULT_QUESTION_URL, newQuestion);

        ResponseEntity<Question> response = template().getForEntity(location, Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update() {
        String location = createResource(DEFAULT_QUESTION_URL, newQuestion);
        Question savedQuestion = getResource(location, Question.class);

        Question updatedQuestion = new Question(savedQuestion.getId(), "오늘의 미션은?", "세차하기");

        ResponseEntity<Question> response = basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updatedQuestion), Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedQuestion.equalsTitleAndContents(response.getBody())).isTrue();
    }

    @Test
    public void update_다른_사람() {
        String location = createResource(DEFAULT_QUESTION_URL, newQuestion);
        User otherUser = newUser(2L);
        Question savedQuestion = getResource(location, Question.class);

        Question updatedQuestion = new Question(savedQuestion.getId(), "오늘의 미션은?", "세차하기");

        ResponseEntity<Question> response = basicAuthTemplate(otherUser).exchange(location, HttpMethod.PUT, createHttpEntity(updatedQuestion), Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() {
        String location = createResource(DEFAULT_QUESTION_URL, newQuestion);

        basicAuthTemplate().delete(location);

        Question dbQuestion = getResource(location, Question.class);
        assertThat(dbQuestion.isDeleted()).isTrue();
    }

    @Test
    public void delete_다른_사람() {
        String location = createResource(DEFAULT_QUESTION_URL, newQuestion);
        User otherUser = newUser(2L);

        basicAuthTemplate(otherUser).delete(location);

        Question dbQuestion = getResource(location, Question.class);
        assertThat(dbQuestion.isDeleted()).isFalse();
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}
