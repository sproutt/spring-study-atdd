package codesquad.web;

import codesquad.domain.Answer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    private static final String ANSWER_API = "/api/answers";

    private Answer answer;
    private String resourceLocation;

    @Before
    public void setUp(){
        answer = new Answer(defaultUser(), "contents");
        resourceLocation = createResource(ANSWER_API, answer, defaultUser());
    }

    @Test
    public void create() {
        answer = getResource(resourceLocation, Answer.class, defaultUser());
        assertThat(answer).isNotNull();
    }

    @Test
    public void create_failed() {
        ResponseEntity<Void> response = template().postForEntity(ANSWER_API, answer, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void show(){

    }

    @Test
    public void update() {

    }

    @Test
    public void update_failed() {

    }

    @Test
    public void delete() {

    }

    @Test
    public void delete_failed() {

    }
}
