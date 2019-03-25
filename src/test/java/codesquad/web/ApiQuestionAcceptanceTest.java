package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    final private static String DEFAULTQUESTIONURL = "/api/questions";

    @Autowired
    UserRepository userRepository;

    User anotherUser;

    @Before
    public void setUp() throws UnAuthenticationException {
        anotherUser = userRepository.findByUserId("sanjigi").orElseThrow(UnAuthenticationException::new);
    }

    @Test
    public void create() throws Exception {
        Question newQuestion = new Question("testQuestion1", "testContents");
        String location = createResource(DEFAULTQUESTIONURL,newQuestion);

        Question dbQuestion = basicAuthTemplate(defaultUser()).getForObject(location, Question.class);
        assertThat(dbQuestion).isNotNull();
    }

    @Test
    public void update() throws Exception {

        Question newQuestion = new Question("testQuestion2", "testContents");
        String location = createResource(DEFAULTQUESTIONURL,newQuestion);

        Question updateQuestion = new Question("updateQuestion1", "updateContents");
        ResponseEntity<Question> responseEntity =
                basicAuthTemplate(defaultUser()).exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Question.class);
        assertThat(updateQuestion.isEqualTitleAndContents(responseEntity.getBody())).isTrue();
    }

    @Test
    public void update_다른_사람() throws Exception {
        Question newQuestion = new Question("testQuestion3", "testContents");
        String location = createResource(DEFAULTQUESTIONURL,newQuestion);

        Question updateQuestion = new Question("updateQuestion2", "updateContents");

        ResponseEntity<Void> responseEntity =
                basicAuthTemplate(anotherUser).exchange(location, HttpMethod.PUT, createHttpEntity(updateQuestion), Void.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() throws Exception {
        Question newQuestion = new Question("testQuestion3", "testContents");
        String location = createResource(DEFAULTQUESTIONURL,newQuestion);

        basicAuthTemplate(defaultUser()).delete(location);

        Question dbQuestion = template().getForObject(location, Question.class);
        assertThat(dbQuestion.isDeleted()).isTrue();
    }

    @Test
    public void delete_다른사람() throws Exception {
        Question newQuestion = new Question("testQuestion4", "testContents");
        String location = createResource(DEFAULTQUESTIONURL,newQuestion);

        basicAuthTemplate(anotherUser).delete(location);

        Question dbQuestion = template().getForObject(location, Question.class);
        assertThat(dbQuestion.isDeleted()).isFalse();
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}
