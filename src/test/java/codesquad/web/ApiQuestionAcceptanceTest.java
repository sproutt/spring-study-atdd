package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import javax.validation.constraints.Null;

import static codesquad.domain.UserTest.SANJIGI;
import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);
    private static final String URL_API_QUESTION = "/api/questions";
    @Test
    public void list(){
        ResponseEntity<Void> response = template().getForEntity(URL_API_QUESTION, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void create(){
        Question newQuestion = new Question("new title", "new context");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);

        assertThat(getResource(location, Question.class , defaultUser())).isNotNull();
    }

    @Test
    public void create_not_login(){
        Question newQuestion = new Question("new title1", "new context1");
        ResponseEntity<Void> response = template().postForEntity(URL_API_QUESTION, newQuestion, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void detail(){
        Question newQuestion = new Question("new title2", "new context2");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);
        Question question= template().getForObject(location, Question.class);

        assertThat(question).isNotNull();
    }

    @Test
    public void update() {
        Question newQuestion = new Question("new title3", "new context3");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);
        Question original = basicAuthTemplate().getForObject(location, Question.class);
        Question updateQuestion = new Question("update3", "update3");

        ResponseEntity<Question> response = basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion),Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo(updateQuestion.getTitle());
    }

    @Test
    public void update_not_login(){
        Question newQuestion = new Question("new title4", "new context4");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);
        Question original = basicAuthTemplate().getForObject(location, Question.class);
        Question updateQuestion = new Question("update4", "update4");

        ResponseEntity<Question> response = template().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion),Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(original.getTitle()).isEqualTo(template().getForObject(location, Question.class).getTitle());
    }

    @Test
    public void update_not_match_user(){
        Question newQuestion = new Question("new title5", "new context5");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);
        Question original = basicAuthTemplate().getForObject(location, Question.class);
        Question updateQuestion = new Question("update5", "update5");

        ResponseEntity<Question> response = basicAuthTemplate(SANJIGI).exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion),Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(original.getTitle()).isEqualTo(template().getForObject(location, Question.class).getTitle());
    }

    @Test
    public void delete(){
        Question newQuestion = new Question("new title6", "new context6");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);

        ResponseEntity<Void> response =  basicAuthTemplate().exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(basicAuthTemplate().getForObject(location, Question.class)).isNull();
    }

    @Test
    public void delete_not_login(){
        Question newQuestion = new Question("new title7", "new context7");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);

        ResponseEntity<Void> response = template().exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(template().getForObject(location, Question.class)).isNotNull();
    }

    @Test
    public void delete_not_match_user(){
        Question newQuestion = new Question("new title8", "new context8");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);

        ResponseEntity<Void> response = basicAuthTemplate(SANJIGI).exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(template().getForObject(location, Question.class)).isNotNull();
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}
