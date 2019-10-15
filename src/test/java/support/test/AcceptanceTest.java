package support.test;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

  private static final String DEFAULT_LOGIN_USER = "javajigi";
  private static final Long DEFAULT_QUESTION_ID = 1L;
  private static final Long DEFAULT_ANSWER_ID = 1L;

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

  protected User defaultUser() {
    return findByUserId(DEFAULT_LOGIN_USER);
  }

  protected Question defaultQuestion(){
    return findByQuestionId(DEFAULT_QUESTION_ID);
  }

  protected User findByUserId(String userId) {
    return userRepository.findByUserId(userId).get();
  }

  protected Question findByQuestionId(Long questionId){
    return questionRepository.findById(questionId).get();
  }

  protected Answer findByAnswerId(Long answerId){
    return answerRepository.findById(answerId).get();
  }

  protected Answer defaultAnswer(){
    return findByAnswerId(DEFAULT_ANSWER_ID);
  }

}
