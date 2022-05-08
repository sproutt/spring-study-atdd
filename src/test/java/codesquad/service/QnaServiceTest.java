package codesquad.service;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void create_question() {
        User loginUser = new User("javajigi", "test", "자바지기", "javajigi@slipp.net");
        Question question = new Question("오늘의 미션은?", "자동차 경주 게임");
        question.writeBy(loginUser);
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        when(questionRepository.save(question)).thenReturn(question);

        Question savedQuestion = qnaService.create(loginUser, question);

        assertThat(qnaService.findById(savedQuestion.getId()).get().getContents()).isEqualTo(question.getContents());
    }

    @Test
    public void findById_success() {
        User loginUser = new User("javajigi", "test", "자바지기", "javajigi@slipp.net");
        Question question = new Question("오늘의 미션은?", "자동차 경주 게임");
        question.writeBy(loginUser);
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
        when(questionRepository.save(question)).thenReturn(question);

        Question savedQuestion = qnaService.create(loginUser, question);
        Question findQuestion = qnaService.findById(savedQuestion.getId()).get();

        assertThat(findQuestion).isEqualTo(savedQuestion);
    }
}
