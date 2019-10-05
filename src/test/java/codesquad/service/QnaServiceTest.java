package codesquad.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.domain.UserTest;
import codesquad.exception.UnAuthenticationException;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerRepository answerRepository;

    private final Long QUESTION_ID = 1L;
    private final Long ANSWER_ID = 1L;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void updateQuestion_sucess() throws Exception {
        User loginUser = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(loginUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question targetQuestion = new Question("update_title", "update_contents");

        Question resultQuestion = qnaService.update(loginUser, QUESTION_ID, targetQuestion);
        assertThat(resultQuestion.getContents(), is(targetQuestion.getContents()));
        assertThat(resultQuestion.getTitle(), is(targetQuestion.getTitle()));

    }

    @Test(expected = UnAuthenticationException.class)
    public void updateQuestion_failed_when_no_login() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question targetQuestion = new Question("update_title", "update_contents");

        qnaService.update(null, QUESTION_ID, targetQuestion);
    }

    @Test(expected = UnAuthenticationException.class)
    public void updateQuestion_failed_when_other_user() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        User otherUser = UserTest.SANJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question targetQuestion = new Question("update_title", "update_contents");
        qnaService.update(otherUser, QUESTION_ID, targetQuestion);
    }

    @Test
    public void deleteQuestion_success() throws Exception {
        User loginUser = UserTest.JAVAJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(loginUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(loginUser, QUESTION_ID);
        assertThat(questionRepository.findById(QUESTION_ID),is(Optional.empty()));
    }

    @Test(expected = UnAuthenticationException.class)
    public void deleteQuestion_failed_when_no_login() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(null, QUESTION_ID);
    }

    @Test(expected = UnAuthenticationException.class)
    public void deleteQuestion_failed_when_other_user() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        User otherUser = UserTest.SANJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(otherUser, QUESTION_ID);
    }

    @Test
    public void addAnswer_success() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);

        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Answer answer = qnaService.addAnswer(originUser, QUESTION_ID, "test_댓글");
        assertThat(answer.getContents(), is("test_댓글"));
        assertThat(answer.getQuestion(), is(question));

    }

    @Test
    public void addAnswer_failed_when_other_user() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        User otherUser = UserTest.SANJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);

        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Answer answer = qnaService.addAnswer(otherUser, QUESTION_ID, "test_댓글");
        assertThat(answer.getContents(), is("test_댓글"));
        assertThat(answer.getQuestion(), is(question));
    }

    @Test(expected = UnAuthenticationException.class)
    public void addAnswer_failed_when_no_login() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);

        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        qnaService.addAnswer(null, QUESTION_ID, "test_댓글");
    }

    @Test
    public void deleteAnswer_success() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);
        Answer answer = new Answer(ANSWER_ID, originUser, question,"test_댓글");

        when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.of(answer));

        qnaService.deleteQuestion(originUser, QUESTION_ID);

        assertThat(answerRepository.findById(ANSWER_ID), is(Optional.empty()));
    }

    @Test(expected = UnAuthenticationException.class)
    public void deleteAnswer_failed_when_no_login() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);
        Answer answer = new Answer(ANSWER_ID, originUser, question,"test_댓글");

        when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.of(answer));


        qnaService.deleteQuestion(null, 1L);
    }

    @Test(expected = UnAuthenticationException.class)
    public void deleteAnswer_failed_when_other_user() throws Exception {
        User originUser = UserTest.JAVAJIGI;
        User otherUser = UserTest.SANJIGI;
        Question question = new Question(QUESTION_ID, "test_title", "test_contents");
        question.writeBy(originUser);
        Answer answer = new Answer(ANSWER_ID, originUser, question,"test_댓글");

        when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.of(answer));

        qnaService.deleteQuestion(otherUser, ANSWER_ID);
    }
}
