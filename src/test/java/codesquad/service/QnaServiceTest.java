package codesquad.service;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static codesquad.domain.UserTest.JAVAJIGI;
import static codesquad.domain.UserTest.SANJIGI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    private MockMvc mvc;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    public static Question question1;
    public static Answer answer3;
    public static Answer answer1;
    public static Answer answer2;

    @Before
    public void init() {
        question1 = new Question("title", "context");
        question1.setId(1);
        question1.writeBy(JAVAJIGI);
        answer1 = new Answer(1L, JAVAJIGI, question1, "answer title1");
        answer2 = new Answer(2L, JAVAJIGI, question1, "answer title2");
        answer3 = new Answer(3L, SANJIGI, question1, "answer title3");
        question1.addAnswer(answer1);
        question1.addAnswer(answer2);
    }

    @Test
    public void delete_question_with_answers() {
        when(questionRepository.findByIdAndDeleted(1L, false)).thenReturn(Optional.of(question1));
        when(questionRepository.save(question1)).thenReturn(question1);

        qnaService.delete(JAVAJIGI, question1.getId());

        assertThat(question1.isDeleted()).isEqualTo(true);
        assertThat(question1.getAnswers().stream().filter(answer -> !answer.isDeleted())).isEmpty();
    }

    @Test
    public void add_answer() {
        when(questionRepository.findByIdAndDeleted(1L, false)).thenReturn(Optional.of(question1));

        qnaService.addAnswer(JAVAJIGI, question1.getId(), "add content");

        assertThat(question1.getAnswers().stream().filter(answer -> answer.getContents().equals("add content"))).isNotNull();
    }

    @Test
    public void delete_answer() {
        when(answerRepository.findByIdAndDeleted(answer3.getId(), false)).thenReturn(Optional.of(answer3));
        when(answerRepository.save(answer3)).thenReturn(answer3);

        Answer deletedAnswer = qnaService.deleteAnswer(JAVAJIGI, answer3.getId());

        assertThat(deletedAnswer.isDeleted()).isEqualTo(true);
    }

}
