package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.newUser;
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
        Question newQuestion = new Question("title2", "contents2");
        newQuestion.writeBy(user);

        ResponseEntity<Void> response = template().postForEntity("/api/questions", newQuestion, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void show() {
        User user = defaultUser();
        Question newQuestion = new Question("title3", "contents3");
        newQuestion.writeBy(user);

        ResponseEntity<Void> response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, Void.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        response = template().getForEntity(location, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update() {
        User user = defaultUser();
        Question newQuestion = new Question("title4", "contents4");
        newQuestion.writeBy(user);

        ResponseEntity<Void> response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, Void.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Question original = template().getForObject(location, Question.class);
        Question updateQuestion = new Question("updatetitle1", "updatecontents1");

        ResponseEntity<Question> responseEntity =
                basicAuthTemplate(user)
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateQuestion.getContents()).isEqualTo(responseEntity.getBody().getContents());
        assertThat(updateQuestion.getTitle()).isEqualTo(responseEntity.getBody().getTitle());

    }

    @Test
    public void update_다른_사람() {
        User user = newUser("testuser1");
        ResponseEntity<Void> response = template().postForEntity("/api/users", user, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Question newQuestion = new Question("title5", "contents5");
        newQuestion.writeBy(user);

        response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, Void.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Question original = template().getForObject(location, Question.class);
        Question updateQuestion = new Question("updatetitle2", "updatecontents2");

        ResponseEntity<Question> responseEntity =
                basicAuthTemplate(defaultUser())
                .exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() {
        User user = defaultUser();
        Question newQuestion = new Question("title6", "contents6");
        newQuestion.writeBy(user);

        ResponseEntity<Void> response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, Void.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Question> responseEntity =
                basicAuthTemplate(user)
                .exchange(location, HttpMethod.DELETE, createHttpEntity(null), Question.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().isDeleted()).isTrue();
    }

    @Test
    public void delete_다른_사람() {
        User user = newUser("testuser2");
        ResponseEntity<Void> response = template().postForEntity("/api/users", user, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Question newQuestion = new Question("title7", "contents7");
        newQuestion.writeBy(user);

        response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, Void.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Question> responseEntity =
                basicAuthTemplate(defaultUser())
                .exchange(location, HttpMethod.DELETE, createHttpEntity(null), Question.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}
