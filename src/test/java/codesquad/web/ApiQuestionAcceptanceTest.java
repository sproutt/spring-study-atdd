package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final String QUESTION_API = "/api/questions";

    private Question newQuestion;
    @Autowired
    private QuestionRepository questionRepository;

    @Before
    public void setUp() {
        newQuestion = new Question("newTitle", "newContents");
    }

    @Test
    public void create() {
        String location = createResource("/api/questions", newQuestion);
        Question question = getResource(location, Question.class, defaultUser());
        assertThat(question).isNotNull();
    }

    @Test
    public void create_no_login() {
        ResponseEntity<Void> response = template().postForEntity(QUESTION_API, newQuestion, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void show() {
        String location = createResource("/api/questions", newQuestion);
        Question question = getResource(location, Question.class, defaultUser());
        assertThat(question.getTitle()).isEqualTo(newQuestion.getTitle());
        assertThat(question.getContents()).isEqualTo(newQuestion.getContents());
    }

    @Test
    public void update(){

    }

    @Test
    public void update_failed(){

    }
}
