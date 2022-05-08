package codesquad.service;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    @Test
    @DisplayName("service 질문 생성 테스트")
    void create() throws Exception {
        Question question = createQuestion(1);

        when(questionRepository.save(question)).thenReturn(question);

        Question savedQuestion = qnaService.create(createUser(1), question);

        assertAll(
                () -> assertThat(question).isEqualTo(savedQuestion),
                () -> assertThat(savedQuestion.getContents()).isEqualTo("aaaa")
        );
    }

    @Test
    @DisplayName("service 질문 목록 조회 테스트")
    void list() throws Exception {
        //given
        List<Question> questions = Arrays.asList(createQuestion(1), createQuestion(2));

        //when
        when(questionRepository.findByDeleted(false)).thenReturn(questions);

        List<Question> savedQuestion = (List<Question>) qnaService.findAll();

        //then
        assertAll(
                () -> assertThat(savedQuestion.size()).isEqualTo(2),
                () -> assertThat(savedQuestion.get(0).getTitle()).isEqualTo(questions.get(0).getTitle()),
                () -> assertThat(savedQuestion.get(1).getTitle()).isEqualTo(questions.get(1).getTitle())
        );
    }

    private User createUser(int id) {
        return new User("userId" + id, "password", "name", "email");
    }

    private Question createQuestion(int id) {
        return new Question("title" + id, "aaaa");
    }
}