package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    @Test
    public void post() {
        User user = defaultUser();
        Question question = defaultQuestion();
        Answer newAnswer = new Answer(1l, user, question, "contents1");

        ResponseEntity<Void> response =
                basicAuthTemplate(user).postForEntity(String.format("/api/questions/{id}/answers", question.getId()), newAnswer, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
