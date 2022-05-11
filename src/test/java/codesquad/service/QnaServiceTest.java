package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    private static final Logger log = LoggerFactory.getLogger(QnaServiceTest.class);
    private final Long USER_ID = 1L;
    private final Long QUESTION_ID = 1L;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void create_question() {
        User loginUser = UserTest.newUser(USER_ID);
        Question question = QuestionTest.newQuestion(QUESTION_ID, loginUser);
        when(qnaService.create(loginUser, question)).thenReturn(question);

        Question savedQuestion = qnaService.create(loginUser, question);

        assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle());
        assertThat(savedQuestion.getContents()).isEqualTo(question.getContents());
        assertThat(savedQuestion.getWriter()).isEqualTo(loginUser);
    }

    @Test
    public void findById_success() {
        User loginUser = UserTest.newUser(USER_ID);
        Question question = QuestionTest.newQuestion(QUESTION_ID, loginUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question savedQuestion = qnaService.findById(QUESTION_ID);

        assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle());
        assertThat(savedQuestion.getContents()).isEqualTo(question.getContents());
    }

    @Test(expected = EntityNotFoundException.class)
    public void findById_fail() {
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.empty());

        qnaService.findById(QUESTION_ID);
    }

    @Test
    public void update_question_success() {
        User loginUser = UserTest.newUser(USER_ID);
        Question question = QuestionTest.newQuestion(QUESTION_ID, loginUser);
        Question targetQuestion = QuestionTest.updatedQuestion(QUESTION_ID, "오늘의 할 일은?", "자동차 주차하기", loginUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        Question updatedQuestion = qnaService.update(loginUser, QUESTION_ID, targetQuestion);

        assertThat(updatedQuestion.getTitle()).isEqualTo(targetQuestion.getTitle());
        assertThat(updatedQuestion.getContents()).isEqualTo(targetQuestion.getContents());
        assertThat(updatedQuestion.getWriter()).isEqualTo(targetQuestion.getWriter());
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_failed_when_writer_mismatch() {
        User user = UserTest.newUser(USER_ID);
        User loginUser = UserTest.SANJIGI;
        Question question = QuestionTest.newQuestion(QUESTION_ID, user);
        Question targetQuestion = QuestionTest.updatedQuestion(QUESTION_ID, "오늘의 할 일은?", "자동차 주차하기", loginUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        qnaService.update(loginUser, QUESTION_ID, targetQuestion);
    }

    @Test(expected = EntityNotFoundException.class)
    public void update_failed_when_entity_not_found() {
        User loginUser = UserTest.newUser(USER_ID);
        Question targetQuestion = QuestionTest.updatedQuestion(QUESTION_ID, "오늘의 할 일은?", "자동차 주차하기", loginUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.empty());

        qnaService.update(loginUser, QUESTION_ID, targetQuestion);
    }

    @Test
    public void delete_success() throws Exception {
        User loginUser = UserTest.newUser(USER_ID);
        Question question = QuestionTest.newQuestion(QUESTION_ID, loginUser);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(loginUser, question.getId());

        assertThat(question.isDeleted()).isTrue();
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_failed_when_writer_mismatch() throws Exception {
        User user = UserTest.newUser(USER_ID);
        User loginUser = UserTest.SANJIGI;
        Question question = QuestionTest.newQuestion(QUESTION_ID, user);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

        qnaService.deleteQuestion(loginUser, question.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_failed_when_entity_not_found() throws Exception {
        User loginUser = UserTest.newUser(USER_ID);
        when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.empty());

        qnaService.deleteQuestion(loginUser, QUESTION_ID);
    }
}
