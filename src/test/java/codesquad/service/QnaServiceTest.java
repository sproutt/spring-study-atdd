package codesquad.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.QuestionTest;
import codesquad.domain.User;
import codesquad.domain.UserTest;
import codesquad.exception.UnAuthenticationException;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.junit.Before;
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
    private final Long ERROR_ID = 111L;

    private User originUser = UserTest.JAVAJIGI;
    private User otherUser = UserTest.SANJIGI;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void updateQuestion_success() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question targetQuestion = new Question("update_title", "update_contents");
        Question resultQuestion = qnaService.update(originUser, QUESTION_ID, targetQuestion);
        assertThat(resultQuestion.getContents(), is(targetQuestion.getContents()));
        assertThat(resultQuestion.getTitle(), is(targetQuestion.getTitle()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateQuestion_not_found() throws Exception {
        Question targetQuestion = new Question("update_title", "update_contents");
        qnaService.update(originUser, ERROR_ID, targetQuestion);
    }

    @Test(expected = UnAuthenticationException.class)
    public void updateQuestion_failed_when_no_login() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question targetQuestion = new Question("update_title", "update_contents");
        qnaService.update(null, QUESTION_ID, targetQuestion);
    }

    @Test(expected = UnAuthenticationException.class)
    public void updateQuestion_failed_when_other_user() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question targetQuestion = new Question("update_title", "update_contents");
        qnaService.update(otherUser, QUESTION_ID, targetQuestion);
    }

    @Test
    public void deleteQuestion_success() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question deletedQuestion = qnaService.deleteQuestion(originUser, QUESTION_ID);
        assertThat(deletedQuestion.isDeleted(), is(true));
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteQuestion_not_found() throws Exception {
        qnaService.deleteQuestion(originUser, ERROR_ID);
    }

    @Test(expected = UnAuthenticationException.class)
    public void deleteQuestion_failed_when_other_user() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(otherUser, QUESTION_ID);
    }

    @Test
    public void addAnswer_success() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Answer answer = qnaService.addAnswer(originUser, QUESTION_ID, "test_댓글");
        assertThat(answer.getContents(), is("test_댓글"));
        assertThat(answer.getQuestion(), is(question));
    }

    @Test
    public void addAnswer_when_other_user() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Answer answer = qnaService.addAnswer(otherUser, QUESTION_ID, "test_댓글");
        assertThat(answer.getContents(), is("test_댓글"));
        assertThat(answer.getQuestion(), is(question));
    }


    @Test(expected = EntityNotFoundException.class)
    public void deleteAnswer_not_found() throws Exception {
        qnaService.deleteAnswer(originUser, ERROR_ID);
    }


    @Test(expected = UnAuthenticationException.class)
    public void deleteAnswer_failed_when_other_user() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        Answer answer = new Answer(ANSWER_ID, originUser, question, "test_댓글");
        when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.of(answer));

        qnaService.deleteAnswer(otherUser, ANSWER_ID);
    }

    @Test
    public void deleteAnswer_success() throws Exception {
        Question question = QuestionTest.newQuestion(originUser, QUESTION_ID);
        Answer answer = new Answer(ANSWER_ID, originUser, question, "test_댓글");
        when(answerRepository.findById(ANSWER_ID)).thenReturn(Optional.of(answer));

        Answer deletedAnswer = qnaService.deleteAnswer(originUser, ANSWER_ID);
        assertThat(deletedAnswer.isDeleted(), is(true));
    }
}
