package codesquad.web;

import codesquad.NullEntityException;
import codesquad.UnAuthenticationException;
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

    Question defaultQuestion;

    @Before
    public void setUp() throws NullEntityException {
        defaultQuestion = questionRepository.findById((long) 1).orElseThrow(NullEntityException::new);
    }

    @Test
    public void create() throws Exception {
        Answer newAnswer = new Answer("testAnswer1");

        ResponseEntity<Void> response = basicAuthTemplate(defaultUser()).postForEntity("/api/questions/"+defaultQuestion.getId(), newAnswer, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String location = response.getHeaders().getLocation().getPath();

        Answer dbAnswer = basicAuthTemplate(defaultUser()).getForObject(location, Answer.class);
        assertThat(dbAnswer).isNotNull();
    }

    private HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}
