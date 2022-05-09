package codesquad.service;

import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.web.dto.QuestionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    @DisplayName("질문 단건 조회")
    void show_question() throws Exception {
        //given
        Question question = createQuestion(1);
        question.setId(1L);

        //when
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        //then
        Question savedQuestion = qnaService.findById(question.getId()).get();

        assertThat(question.getId()).isEqualTo(savedQuestion.getId());
    }

    @Test
    @DisplayName("질문 작성자가 아닌 유저가 수정할 경우 UnAuthenticationException 발생")
    void update_fail_not_match_user() throws Exception {
        //given
        Question question = createQuestion(1);
        Question updatedQuestion = createQuestion(2);
        updatedQuestion.setContents("contents");
        question.setId(1L);
        User user1 = createUser(1);
        user1.setId(1L);

        User user2 = createUser(2);
        user2.setId(2L);

        question.writeBy(user1);
        updatedQuestion.writeBy(user1);

        QuestionDto updatedQuestionDto = updatedQuestion.toDto();

        //then
        assertThrows(
                UnAuthenticationException.class, () -> qnaService.update(user2, question.getId(), updatedQuestionDto)
        );
    }

    @Test
    @DisplayName("질문 작성자가 질문을 수정할 경우 수정된 질문을 반환한다")
    void update_success() throws Exception{
        //given
        Question question = createQuestion(1);
        Question updatedQuestion = createQuestion(2);
        updatedQuestion.setContents("contents");
        question.setId(1L);
        updatedQuestion.setId(1L);

        User user1 = createUser(1);
        user1.setId(1L);

        question.writeBy(user1);
        updatedQuestion.writeBy(user1);

        QuestionDto updatedQuestionDto = updatedQuestion.toDto();


        //when
        when(questionRepository.findById(1L)).thenReturn(Optional.of(updatedQuestion));

        Question savedQuestion = qnaService.update(user1, question.getId(), updatedQuestionDto);

        //then
        assertAll(
                () -> assertThat(savedQuestion.getContents()).isNotEqualTo(question.getContents()),
                () -> assertThat(savedQuestion.getTitle()).isNotEqualTo(question.getTitle()),
                () -> assertThat(savedQuestion.getId()).isEqualTo(question.getId())
        );
    }

    private User createUser(int id) {
        return new User("userId" + id, "password", "name", "email");
    }

    private Question createQuestion(int id) {
        return new Question("title" + id, "aaaa");
    }
}