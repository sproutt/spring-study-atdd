package codesquad.web;

import codesquad.NullEntityException;
import codesquad.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    final private static String DEFAULTANSWERURL = "/api/questions/1/answers";

    User anotherUser;

    @Before
    public void setUp() throws NullEntityException {
        anotherUser = userRepository.findByUserId("sanjigi").orElseThrow(NullEntityException::new);
    }

    @Test
    public void create() throws Exception {
        String testContents = "testContents1";

        ResponseEntity<Void> response = basicAuthTemplate(defaultUser()).postForEntity(DEFAULTANSWERURL, testContents, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        Answer dbAnswer = basicAuthTemplate().getForObject(location, Answer.class);
        assertThat(dbAnswer).isNotNull();
    }

    @Test
    public void update() throws Exception {
        String testContents = "testContents2";

        ResponseEntity<Void> response = basicAuthTemplate(defaultUser()).postForEntity(DEFAULTANSWERURL, testContents, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        String updateString = "testString";
        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(defaultUser()).exchange(location, HttpMethod.PUT, createHttpEntity(updateString), Answer.class);
        assertThat(responseEntity.getBody().getContents()).isEqualTo(updateString);
    }

    @Test
    public void update_다른_사람() throws Exception {
        String testContents = "testContents3";

        ResponseEntity<Void> response = basicAuthTemplate(defaultUser()).postForEntity(DEFAULTANSWERURL, testContents, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        String updateString = "testString";
        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(anotherUser).exchange(location, HttpMethod.PUT, createHttpEntity(updateString), Answer.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() throws Exception {
        String testContents = "testContents4";

        ResponseEntity<Void> response = basicAuthTemplate(defaultUser()).postForEntity(DEFAULTANSWERURL, testContents, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        basicAuthTemplate(defaultUser()).delete(location);

        Answer dbAnswer = template().getForObject(location, Answer.class);
        assertThat(dbAnswer.isDeleted()).isTrue();
    }

    @Test
    public void delete_다른사람() throws Exception {
        String testContents = "testContents5";

        ResponseEntity<Void> response = basicAuthTemplate(defaultUser()).postForEntity(DEFAULTANSWERURL, testContents, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        basicAuthTemplate(anotherUser).delete(location);

        Answer dbAnswer = template().getForObject(location, Answer.class);
        assertThat(dbAnswer.isDeleted()).isFalse();
    }


    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }

}