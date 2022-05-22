package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.domain.UserTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
    public static final Logger log = LoggerFactory.getLogger(QnaServiceTest.class);

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QnaService qnaService;

    private User authorizedUser;
    private User unAuthorizedUser;

    @Before
    public void setUp() {
        authorizedUser = UserTest.JAVAJIGI;
        unAuthorizedUser = UserTest.SANJIGI;

    }

    public Question makeDefaultQuestion() {
        Question defaultQuestion = new Question("title1", "contents1");
        defaultQuestion.setId(1L);
        defaultQuestion.setWriter(authorizedUser);

        return defaultQuestion;
    }

    @Test
    public void 질문을_저장할_때_작성자_이름과_질문이_잘_저장되는지_테스트() {
        //given
        Question beforeSavedQuestion = makeDefaultQuestion();
        when(questionRepository.save(beforeSavedQuestion)).thenReturn(beforeSavedQuestion);

        //when
        Question savedQuestion = qnaService.create(authorizedUser, beforeSavedQuestion);
        log.debug("beforeSavedQuestion ={}", beforeSavedQuestion);
        log.debug("savedQuestion ={}", savedQuestion);

        //then
        assertThat(savedQuestion.getWriter()).isEqualTo(authorizedUser);
        assertThat(beforeSavedQuestion).isEqualTo(savedQuestion);
    }

    @Test
    public void 질문ID로_질문을_조회할_떄_질문이_잘_반환되는지_테스트() {
        //given
        Question question = makeDefaultQuestion();

        log.debug("afterSavedQuestion Id ={}", question.getId());
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        //when
        Question afterFindByIdQuestion = qnaService.findById(question.getId());

        //then
        assertThat(question).isEqualTo(afterFindByIdQuestion);
    }

    @Test
    public void 작성자와_로그인한_사용자가_일치할_때_질문이_잘_삭제되는지_테스트() throws CannotDeleteException {
        //given
        Question question = makeDefaultQuestion();

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        //when
        qnaService.deleteQuestion(authorizedUser, question.getId());
        log.debug("question isDeleted ={}", question.isDeleted());

        //then
        assertThat(question.isDeleted()).isEqualTo(true);
    }

    @Test
    public void 작성자와_로그인한_사용자가_일치하지_않을_때_질문이_삭제되지_않는지_테스트() throws CannotDeleteException {
        //given
        Question question = makeDefaultQuestion();

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        //when
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> qnaService.deleteQuestion(unAuthorizedUser, question.getId()));

        //then
        log.debug("question isDeleted ={}", question.isDeleted());
        assertThat(question.isDeleted()).isEqualTo(false);
        assertThat(noSuchElementException).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void 작성자와_로그인한_사용자가_일치할_때_질문이_잘_수정되는지_테스트() {
        //given
        Question question = makeDefaultQuestion();
        Question newQuestion = new Question("title2", "content2");

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        //when
        Question afterUpdateQuestion = qnaService.update(authorizedUser, question.getId(), newQuestion);

        log.debug("afterUpdateQuestion title ={}", afterUpdateQuestion.getTitle());
        log.debug("afterUpdateQuestion contents ={}", afterUpdateQuestion.getContents());
        log.debug("afterUpdateQuestion writer ={}", afterUpdateQuestion.getWriter());

        //then
        assertThat(question).isEqualTo(afterUpdateQuestion);
        assertThat(question.getWriter()).isEqualTo(afterUpdateQuestion.getWriter());
    }

    @Test
    public void 작성자와_로그인한_사용자가_일치하지_않을_때_질문이_수정되지_않는지_테스트() {
        //given
        Question question = makeDefaultQuestion();
        Question newQuestion = new Question("title2", "contents2");

        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        //when
        NoSuchElementException noSuchElementException = assertThrows(NoSuchElementException.class, () -> qnaService.update(unAuthorizedUser, question.getId(), newQuestion));

        //then
        assertThat(noSuchElementException).isInstanceOf(NoSuchElementException.class);
    }
}