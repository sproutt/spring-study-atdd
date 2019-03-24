package codesquad.web;

import codesquad.domain.Answer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static codesquad.domain.QuestionTest.SANJIGI;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiAnswerAcceptanceTest.class);

    private static final String URL_API_QUESTION_DETAIL = "/api/questions/1";
    private static final String URL_API_ANSWER = URL_API_QUESTION_DETAIL + "/answers";

    @Test
    public void list() {
        ResponseEntity<Iterable> response = template().getForEntity(URL_API_ANSWER, Iterable.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void create() {
        String location = createResourceWithAuth(URL_API_ANSWER, "contents", "answer test");

        assertThat(getResource(location, Answer.class, defaultUser()).getContents()).isEqualTo("answer test");
    }

    @Test
    public void create_not_login() {
        ResponseEntity<String> response = template().postForEntity(URL_API_ANSWER, "answer test1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() {
        String location = createResourceWithAuth(URL_API_ANSWER, "contents", "answer test2");

        ResponseEntity<Void> response = basicAuthTemplate().exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void delete_not_login() {
        String location = createResourceWithAuth(URL_API_ANSWER, "contents", "answer test2");

        ResponseEntity<Void> response = template().exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_unmatch_user() {
        String location = createResourceWithAuth(URL_API_ANSWER, "contents", "answer test2");

        ResponseEntity<Void> response = basicAuthTemplate(SANJIGI).exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_question_also_delete_answer() {
        String location = createResourceWithAuth(URL_API_ANSWER, "contents", "answer test3");
        Answer answer = getResource(location, Answer.class, defaultUser());

        ResponseEntity<Void> response = basicAuthTemplate().exchange(URL_API_QUESTION_DETAIL, HttpMethod.DELETE, createHttpEntity(null), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Answer> answerResponse = basicAuthTemplate().getForEntity(URL_API_QUESTION_DETAIL + "/answers/" + answer.getId(), Answer.class);
        assertThat(answerResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
