package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionDTO;
import codesquad.domain.User;
import codesquad.exception.QuestionDeletedException;
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
        QuestionDTO newQuestionDTO = new QuestionDTO("new title", "new context");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestionDTO);

        assertThat(getResource(location, Question.class , defaultUser())).isNotNull();
        assertThat(getResource(location, Question.class , defaultUser()).getTitle()).isEqualTo("new title");

    }

    @Test
    public void create_not_login(){
        QuestionDTO newQuestionDTO = new QuestionDTO("new title1", "new context1");
        ResponseEntity<Void> response = template().postForEntity(URL_API_QUESTION, newQuestionDTO, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void detail(){
        QuestionDTO newQuestion = new QuestionDTO("new title2", "new context2");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);
        Question question= template().getForObject(location, Question.class);

        assertThat(question).isNotNull();
    }

    @Test
    public void update() {
        QuestionDTO newQuestionDTO = new QuestionDTO("new title3", "new context3");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestionDTO);
        Question original = basicAuthTemplate().getForObject(location, Question.class);
        QuestionDTO updateQuestionDto = new QuestionDTO("update3", "update3");

        ResponseEntity<Question> response = basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestionDto),Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo(updateQuestionDto.getTitle());
        assertThat(response.getBody().getContents()).isEqualTo(updateQuestionDto.getContent());

    }

    @Test
    public void update_not_login(){
        QuestionDTO newQuestion = new QuestionDTO("new title4", "new context4");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);
        Question original = basicAuthTemplate().getForObject(location, Question.class);
        QuestionDTO updateQuestionDto = new QuestionDTO("update4", "update4");

        ResponseEntity<Question> response = template().exchange(location, HttpMethod.PUT, createHttpEntity(new QuestionDTO("update4", "update4")),Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(original.getTitle()).isEqualTo(template().getForObject(location, Question.class).getTitle());
    }

    @Test
    public void update_not_match_user(){
        QuestionDTO newQuestion = new QuestionDTO("new title5", "new context5");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);
        Question original = basicAuthTemplate().getForObject(location, Question.class);
        QuestionDTO updateQuestionDto = new QuestionDTO("update5", "update5");

        ResponseEntity<Question> response = basicAuthTemplate(SANJIGI).exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestionDto),Question.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(original.getTitle()).isEqualTo(template().getForObject(location, Question.class).getTitle());
    }

    @Test
    public void delete() throws Exception{
        QuestionDTO newQuestion = new QuestionDTO("new title6", "new context6");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);

        ResponseEntity<Void> response =  basicAuthTemplate().exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(basicAuthTemplate().getForEntity(location, Question.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void delete_not_login(){
        QuestionDTO newQuestion = new QuestionDTO("new title7", "new context7");
        String location = createResourceWithAuth(URL_API_QUESTION, newQuestion);

        ResponseEntity<Void> response = template().exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(template().getForObject(location, Question.class).isDeleted()).isFalse();
    }

    @Test
    public void delete_not_match_user(){
        QuestionDTO newQuestion = new QuestionDTO("new title8", "new context8");
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
