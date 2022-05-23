package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static codesquad.domain.QuestionTest.newQuestion;
import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void create() {
        Question newQuestion = defaultQuestion();
        ResponseEntity<Question> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        Question dbQuestion = basicAuthTemplate().getForObject(location, Question.class);
        assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void show() {
        ResponseEntity<Question> response = basicAuthTemplate().postForEntity("/api/questions", defaultQuestion(), Question.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        response = template().getForEntity(location, Question.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void update() {
        ResponseEntity<Question> response = basicAuthTemplate().postForEntity("/api/questions", defaultQuestion(), Question.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Question savedQuestion = basicAuthTemplate().getForObject(location, Question.class);

        Question updatedQuestion = new Question(savedQuestion.getId(), "오늘의 미션은?", "세차하기");

        response = basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updatedQuestion), Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedQuestion.equalsTitleAndContents(response.getBody())).isTrue();
    }

    @Test
    public void update_다른_사람() {
        ResponseEntity<Question> response = basicAuthTemplate().postForEntity("/api/questions", defaultQuestion(), Question.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        User otherUser = newUser(2L);
        Question savedQuestion = basicAuthTemplate().getForObject(location, Question.class);

        Question updatedQuestion = new Question(savedQuestion.getId(), "오늘의 미션은?", "세차하기");

        response = basicAuthTemplate(otherUser).exchange(location, HttpMethod.PUT, createHttpEntity(updatedQuestion), Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() {
        ResponseEntity<Question> response = basicAuthTemplate().postForEntity("/api/questions", defaultQuestion(), Question.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Question savedQuestion = basicAuthTemplate().getForObject(location, Question.class);

        basicAuthTemplate().delete(location);

        Question dbQuestion = basicAuthTemplate().getForObject(location, Question.class, defaultUser());
        assertThat(dbQuestion.isDeleted()).isTrue();
    }

    @Test
    public void delete_다른_사람() {
        ResponseEntity<Question> response = basicAuthTemplate().postForEntity("/api/questions", defaultQuestion(), Question.class);
        String location = response.getHeaders().getLocation().getPath();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        User otherUser = newUser(2L);
        Question savedQuestion = basicAuthTemplate().getForObject(location, Question.class);

        basicAuthTemplate(otherUser).delete(location);

        Question dbQuestion = basicAuthTemplate().getForObject(location, Question.class, defaultUser());
        assertThat(dbQuestion.isDeleted()).isFalse();
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}
