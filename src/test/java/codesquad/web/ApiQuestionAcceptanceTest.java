package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static codesquad.domain.QuestionTest.newQuestion;
import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void create() {
        User loginUser = newUser("javajigi");
        Question newQuestion = newQuestion(loginUser);
        ResponseEntity<Question> response = basicAuthTemplate(loginUser).postForEntity("/api/questions", newQuestion, Question.class);
        String location = response.getHeaders().getLocation().getPath();

        Question dbQuestion = basicAuthTemplate(loginUser).getForObject(location, Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(dbQuestion).isNotNull();
    }
}
