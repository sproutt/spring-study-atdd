package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final String QUESTION_API = "/api/questions";

    private List<Question> questions;
    private Question question;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void create() {
        Question newQuestion = new Question("newTitle", "newContents");
        ResponseEntity<Void> response = template().postForEntity(QUESTION_API, newQuestion, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        Question dbQuestion = basicAuthTemplate(defaultUser()).getForObject(location, Question.class);
        assertThat(dbQuestion).isNotNull();
    }
}
