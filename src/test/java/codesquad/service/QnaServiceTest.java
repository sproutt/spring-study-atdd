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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        Question question = createQuestion();

        when(questionRepository.save(question)).thenReturn(question);

        Question savedQuestion = qnaService.create(createUser(), question);

        assertAll(
                () -> assertThat(question).isEqualTo(savedQuestion),
                () -> assertThat(savedQuestion.getContents()).isEqualTo("aaaa")
        );
    }

    private User createUser() {
        return new User("userId", "password", "name", "email");
    }

    private Question createQuestion() {
        return new Question("title", "aaaa");
    }
}