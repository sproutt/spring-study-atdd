package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void post() {
        User user = defaultUser();
        Question newQuestion = new Question("title1", "contents1");
        newQuestion.writeBy(user);

        ResponseEntity<Void> response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void post_no_login() {
        User user = defaultUser();
        Question newQuestion = new Question("title1", "contents1");
        newQuestion.writeBy(user);

        ResponseEntity<Void> response = template().postForEntity("/api/questions", newQuestion, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void show() {
        User user = defaultUser();
        Question newQuestion = new Question("title1", "contents1");
        newQuestion.writeBy(user);

        ResponseEntity<Void> response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String location = response.getHeaders().getLocation().getPath();

        response = template().getForEntity(location, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
