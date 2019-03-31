package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.QuestionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    final private static String DEFAULT_ANSWER_URL = "/api/questions/1/answers";

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void create() throws Exception {
        String testContents = "testContents1";
        String location = createResource(DEFAULT_ANSWER_URL,testContents);

        Answer dbAnswer = getResource(location,Answer.class,defaultUser());
        assertThat(dbAnswer).isNotNull();
    }

    @Test
    public void update() throws Exception {
        String testContents = "testContents2";
        String location = createResource(DEFAULT_ANSWER_URL,testContents);

        String updateString = "testString";
        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(defaultUser()).exchange(location, HttpMethod.PUT, createHttpEntity(updateString), Answer.class);

        assertThat(responseEntity.getBody().getContents()).isEqualTo(updateString);
    }

    @Test
    public void update_다른_사람() throws Exception {
        String testContents = "testContents3";
        String location = createResource(DEFAULT_ANSWER_URL,testContents);

        String updateString = "testString";
        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(anotherUser()).exchange(location, HttpMethod.PUT, createHttpEntity(updateString), Answer.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() throws Exception {
        String testContents = "testContents4";
        String location = createResource(DEFAULT_ANSWER_URL,testContents);

        basicAuthTemplate(defaultUser()).delete(location);

        Answer dbAnswer = getResource(location,Answer.class,defaultUser());
        assertThat(dbAnswer.isDeleted()).isTrue();
    }

    @Test
    public void delete_다른사람() throws Exception {
        String testContents = "testContents5";
        String location = createResource(DEFAULT_ANSWER_URL,testContents);

        basicAuthTemplate(anotherUser()).delete(location);

        Answer dbAnswer = getResource(location,Answer.class,defaultUser());
        assertThat(dbAnswer.isDeleted()).isFalse();
    }


    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }

}