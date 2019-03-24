package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    @Test
    public void post() {
        User user = defaultUser();
        Question question = defaultQuestion();
        Answer newAnswer = new Answer();
        newAnswer.setContents("contents1");

        ResponseEntity<Void> response =
                basicAuthTemplate(user).postForEntity("/api" + question.generateUrl() + "/answers", newAnswer, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void post_no_login() {
        User user = defaultUser();
        Question question = defaultQuestion();
        Answer newAnswer = new Answer();
        newAnswer.setContents("contents2");

        ResponseEntity<Void> response =
                template().postForEntity("/api" + question.generateUrl() + "/answers", newAnswer, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void show() {
        User user = defaultUser();
        Question question = defaultQuestion();
        Answer newAnswer = new Answer();
        newAnswer.setContents("contents3");

        ResponseEntity<Void> response =
                template().getForEntity(createResource("/api" + question.generateUrl() + "/answers", newAnswer, user), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update() {
        User user = defaultUser();
        Question question = defaultQuestion();
        Answer newAnswer = new Answer();
        newAnswer.setContents("contents4");

        Answer updateAnswer = new Answer();
        updateAnswer.setContents("updatecontents1");

        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(user)
                        .exchange(createResource("/api" + question.generateUrl() + "/answers", newAnswer, user), HttpMethod.PUT,
                                createHttpEntity(updateAnswer), Answer.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateAnswer.getContents()).isEqualTo(responseEntity.getBody().getContents());
    }

    @Test
    public void update_다른_사람() {
        User user = newUser("testuser1");
        createResource("/api/users", user);
        Question question = defaultQuestion();
        Answer newAnswer = new Answer();
        newAnswer.setContents("contents5");

        Answer updateAnswer = new Answer();
        updateAnswer.setContents("updatecontents2");

        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(user)
                        .exchange(createResource("/api" + question.generateUrl() + "/answers", newAnswer, user), HttpMethod.PUT,
                                createHttpEntity(updateAnswer), Answer.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
