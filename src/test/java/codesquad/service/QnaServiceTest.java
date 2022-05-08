package codesquad.service;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    private static final Logger log = LoggerFactory.getLogger(QnaServiceTest.class);

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    @Test
    public void create_question() {
        User loginUser = new User("javajigi", "test", "자바지기", "javajigi@slipp.net");
        Question question = new Question("오늘의 미션은?", "자동차 경주 게임");
        question.writeBy(loginUser);
        when(qnaService.create(loginUser, question)).thenReturn(question);

        assertThat(qnaService.create(loginUser, question).getContents()).isEqualTo(question.getContents());
    }

    @Test
    public void findById_success() {
        User loginUser = new User("javajigi", "test", "자바지기", "javajigi@slipp.net");
        Question question = new Question("오늘의 미션은?", "자동차 경주 게임");
        question.writeBy(loginUser);
        when(qnaService.findById(question.getId())).thenReturn(Optional.of(question));

        assertThat(qnaService.findById(question.getId()).get()).isEqualTo(question);
    }
}
