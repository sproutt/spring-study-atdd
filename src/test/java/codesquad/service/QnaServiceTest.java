package codesquad.service;

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
}
