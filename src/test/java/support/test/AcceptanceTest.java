package support.test;

import codesquad.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
    private static final String DEFAULT_LOGIN_USER = "javajigi";
    private static final long DEFAULT_QUESTION_ID = 1;
    private static final long DEFAULT_ANSWER_ID = 1;
    private static final long DEFAULT_DELETE_QUESTION_ID = 99;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    public TestRestTemplate template() {
        return template;
    }

    public TestRestTemplate basicAuthTemplate() {
        return basicAuthTemplate(defaultUser());
    }

    public TestRestTemplate basicAuthTemplate(User loginUser) {
        return template.withBasicAuth(loginUser.getUserId(), loginUser.getPassword());
    }

    protected Question defaultQuestion() {
        return questionRepository.findById(DEFAULT_QUESTION_ID).get();
    }

    protected Question defaultDeletedQuestion() {
        return questionRepository.findById(DEFAULT_DELETE_QUESTION_ID).get();
    }

    protected User defaultUser() {
        return findByUserId(DEFAULT_LOGIN_USER);
    }

    protected User findByUserId(String userId) {
        return userRepository.findByUserId(userId).get();
    }

    protected Answer defaultAnswer() {
        return answerRepository.findById(DEFAULT_ANSWER_ID).get();
    }

    protected ResponseEntity<Question> createQuestionResource(String path, Object bodyPayload) {
        ResponseEntity<Question> response = basicAuthTemplate().postForEntity(path, bodyPayload, Question.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response;
    }

    protected ResponseEntity<Answer> createAnswerResource(String path, Object bodyPayload) {
        ResponseEntity<Answer> response = basicAuthTemplate().postForEntity(path, bodyPayload, Answer.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return response;
    }

    protected <T> T getResource(String location, Class<T> responseType, User loginUser) {
        return basicAuthTemplate(loginUser).getForObject(location, responseType);
    }

    public HttpEntity createHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(body, headers);
    }
}
