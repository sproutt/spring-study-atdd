package codesquad.service;

import codesquad.UnAuthorizedException;
import codesquad.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnswerServiceTest {
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private QuestionService questionService;

    @InjectMocks
    private AnswerService answerService;

    private User authorizedUser;
    private User unAuthorizedUser;

    @Before
    public void setUp() {
        authorizedUser = UserTest.JAVAJIGI;
        unAuthorizedUser = UserTest.SANJIGI;
    }

    @Test
    public void 한사용자가_로그인되어_있을_떄_답변이_잘_작성되는지_테스트() {
        //given
        Question question = makeDefaultQuestion();
        Answer answer = new Answer(authorizedUser, "contents1");
        answer.addToQuestion(question);
        //when
        when(questionService.findById(1L)).thenReturn(question);
        when(answerRepository.save(answer)).thenReturn(answer);

        Answer savedAnswer = answerService.createAnswer(authorizedUser, question.getId(), answer.getContents());
        //then
        assertAll(
                () -> assertThat(savedAnswer.getWriter()).isEqualTo(answer.getWriter()),
                () -> assertThat(savedAnswer.getQuestion()).isEqualTo(answer.getQuestion()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()));
    }

    @Test
    public void 답변이_단건으로_잘_조회되는지_테스트() {
        //given
        Answer answer = new Answer(authorizedUser, "contents1");

        //when
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        Answer savedAnswer = answerService.findByAnswerId(1L);

        //then
        assertAll(
                () -> assertThat(savedAnswer.getWriter()).isEqualTo(answer.getWriter()),
                () -> assertThat(savedAnswer.getQuestion()).isEqualTo(answer.getQuestion()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()));
    }

    @Test
    public void 답변_작성자와_로그인한_사용자가_일치할_때_답변이_잘_수정되는지_테스트() {
        //given
        Answer answer = new Answer(authorizedUser, "contents1");
        String updateContents = "수정된 내용";

        //when
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        Answer updatedAnswer = answerService.updateAnswer(authorizedUser, 1L, updateContents);

        //then
        assertThat(updatedAnswer.getContents()).isEqualTo(answer.getContents());
        assertThat(updatedAnswer.getWriter()).isEqualTo(answer.getWriter());
    }

    @Test
    public void 답변_작성자와_로그인한_사용자가_일치하지_않을_때_답변_수정할시_UnAuthorized를_던지는지_테스트() {
        //given
        Answer answer = new Answer(authorizedUser, "contents1");
        String updateContents = "수정된 내용";

        //when
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        //then
        assertThrows(
                UnAuthorizedException.class, () -> answerService.updateAnswer(unAuthorizedUser, 1L, updateContents));

    }

    @Test
    public void 답변_작성자와_로그인한_사용자가_일치할_때_답변의_삭제상태가_제대로_비뀌는지_테스트() {
        //given
        Answer answer = new Answer(authorizedUser, "contents1");

        //when
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        Answer savedAnswer = answerService.deleteAnswer(authorizedUser, 1L);

        //then
        assertTrue(savedAnswer.isDeleted());
    }

    @Test
    public void 답변_작성자와_로그인한_사용자가_일치하지_않을_때_답변_삭제할시_UnAuthorized를_던지는지_테스트() {
        //given
        Answer answer = new Answer(authorizedUser, "contents1");

        //when
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        //then
        assertThrows(UnAuthorizedException.class, () -> answerService.deleteAnswer(unAuthorizedUser, 1L));
    }

    public Question makeDefaultQuestion() {
        Question defaultQuestion = new Question("title1", "contents1");
        defaultQuestion.setId(1L);
        defaultQuestion.setWriter(authorizedUser);

        return defaultQuestion;
    }
}