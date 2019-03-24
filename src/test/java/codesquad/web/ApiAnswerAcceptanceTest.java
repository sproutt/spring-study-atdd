package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.QuestionDTO;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import java.util.List;

import static codesquad.domain.QuestionTest.SANJIGI;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiAnswerAcceptanceTest.class);

    private static final String URL_API_QUESTION ="/api/questions";
    private StringBuilder urlApiQuestionDetail;
    private StringBuilder urlApiAnswer;

    @Before
    public void init(){
        QuestionDTO newQuestionDTO = new QuestionDTO("new title", "new context");
        urlApiQuestionDetail=new StringBuilder(createResourceWithAuth(URL_API_QUESTION, newQuestionDTO));
        urlApiAnswer =new StringBuilder(urlApiQuestionDetail +"/answers");

        createResourceWithAuth(urlApiAnswer.toString(), "new answer contents1");
        createResourceWithAuth(urlApiAnswer.toString(), "new answer contents2");
    }

    @Test
    public void list(){
        ResponseEntity<List> response = template().getForEntity(urlApiAnswer.toString(), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void create(){
        String location = createResourceWithAuth(urlApiAnswer.toString(), "answer test");

        assertThat(getResource(location, Answer.class, defaultUser()).getContents()).isEqualTo("answer test");
    }

    @Test
    public void create_not_login(){
        ResponseEntity<String> response = template().postForEntity(urlApiAnswer.toString(), "answer test1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete(){
        String location = createResourceWithAuth(urlApiAnswer.toString(), "answer test2");

        ResponseEntity<Void> response = basicAuthTemplate().exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void delete_not_login(){
        String location = createResourceWithAuth(urlApiAnswer.toString(), "answer test2");

        ResponseEntity<Void> response = template().exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_unmatch_user(){
        String location = createResourceWithAuth(urlApiAnswer.toString(), "answer test2");

        ResponseEntity<Void> response = basicAuthTemplate(SANJIGI).exchange(location, HttpMethod.DELETE, createHttpEntity(null), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_question_also_delete_answer(){
        String location = createResourceWithAuth(urlApiAnswer.toString(), "answer test3");
        Answer answer = getResource(location, Answer.class, defaultUser());

        ResponseEntity<Void> response = basicAuthTemplate().exchange(urlApiQuestionDetail.toString(), HttpMethod.DELETE, createHttpEntity(null), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Answer> answerResponse = basicAuthTemplate().getForEntity(urlApiQuestionDetail
                .append("/")
                .append(answer.getId())
                .toString(), Answer.class);
        assertThat(answerResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }

}
