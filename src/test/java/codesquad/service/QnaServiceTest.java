package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.web.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    private User dummyUser;
    private Question dummyQuestion;

    @BeforeEach
    void init() {
        dummyUser = createUser(1L);
        dummyQuestion = createQuestion(1L);
    }

    @Test
    @DisplayName("로그인 한 유저가 질문 생성 요청 시 생성된 질문 반환")
    void create() throws Exception {
        //when
        when(questionRepository.save(dummyQuestion)).thenReturn(dummyQuestion);

        Question savedQuestion = qnaService.create(createUser(1), dummyQuestion);

        //then
        assertAll(
                () -> assertThat(dummyQuestion).isEqualTo(savedQuestion),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(dummyQuestion.getContents())
        );
    }

    @Test
    @DisplayName("질문 목록을 요청할 시 모든 질문 목록을 반환")
    void list() throws Exception {
        //given
        List<Question> questions = Arrays.asList(dummyQuestion, createQuestion(2L));

        //when
        when(questionRepository.findByDeleted(false)).thenReturn(questions);

        List<Question> savedQuestion = (List<Question>) qnaService.findAll();

        //then
        assertAll(
                () -> assertThat(savedQuestion.size()).isEqualTo(2),
                () -> assertThat(savedQuestion.get(0).getTitle()).isEqualTo(questions.get(0).getTitle()),
                () -> assertThat(savedQuestion.get(1).getContents()).isEqualTo(questions.get(1).getContents())
        );
    }

    @Test
    @DisplayName("질문 단건 조회 요청 시 id를 통해 해당 질문 반환")
    void show_question() throws Exception {
        //when
        when(questionRepository.findById(dummyQuestion.getId())).thenReturn(Optional.of(dummyQuestion));

        //then
        Question savedQuestion = qnaService.findById(dummyQuestion.getId()).get();

        assertThat(dummyQuestion.getId()).isEqualTo(savedQuestion.getId());
    }

    @Test
    @DisplayName("질문 작성자가 아닌 유저가 수정할 경우 UnAuthenticationException 발생")
    void update_fail_not_match_user() throws Exception {
        //given
        Question updatedQuestion = createQuestion(2L);
        updatedQuestion.setContents("contents");

        User user = createUser(2L);
        user.setId(2L);

        dummyQuestion.writeBy(dummyUser);
        updatedQuestion.writeBy(dummyUser);

        QuestionDto updatedQuestionDto = updatedQuestion.toDto();

        //when
        when(questionRepository.findById(dummyQuestion.getId()))
                .thenReturn(Optional.of(dummyQuestion));

        //then
        assertThrows(
                UnAuthenticationException.class, () -> qnaService.update(user, dummyQuestion.getId(), updatedQuestionDto)
        );
    }

    @Test
    @DisplayName("질문 작성자가 질문을 수정할 경우 수정된 질문을 반환한다")
    void update_success() throws Exception{
        //given
        Question updatedQuestion = createQuestion(1L);
        updatedQuestion.setContents("contents");
        updatedQuestion.setTitle("title");

        dummyQuestion.writeBy(dummyUser);
        updatedQuestion.writeBy(dummyUser);

        QuestionDto updatedQuestionDto = updatedQuestion.toDto();

        //when
        when(questionRepository.findById(1L)).thenReturn(Optional.of(updatedQuestion));

        Question savedQuestion = qnaService.update(dummyUser, dummyQuestion.getId(), updatedQuestionDto);

        //then
        assertAll(
                () -> assertThat(savedQuestion.getContents()).isNotEqualTo(dummyQuestion.getContents()),
                () -> assertThat(savedQuestion.getTitle()).isNotEqualTo(dummyQuestion.getTitle()),
                () -> assertThat(savedQuestion.getId()).isEqualTo(dummyQuestion.getId())
        );
    }

    @Test
    @DisplayName("질문 작성자가 아닌 유저가 삭제할 경우 UnAuthenticationException 발생")
    void delete_fail() throws Exception{
        //given
        User user = createUser(2L);

        dummyQuestion.writeBy(dummyUser);

        //when
        when(questionRepository.findById(1L)).thenReturn(Optional.of(dummyQuestion));

        //then
        assertThrows(
                CannotDeleteException.class, () -> qnaService.deleteQuestion(user, dummyQuestion.getId())
        );
    }

    @Test
    @DisplayName("질문 작성자가 질문을 삭제할 경우 삭제상태를 true로 변경")
    void delete_success() throws Exception{
        //given
        dummyQuestion.writeBy(dummyUser);

        //when
        when(questionRepository.findById(1L)).thenReturn(Optional.of(dummyQuestion));

        //then
        qnaService.deleteQuestion(dummyUser, dummyQuestion.getId());

        assertTrue(dummyQuestion.isDeleted());
    }

    private User createUser(long id) {
        User user = new User("userId" + id, "password", "name", "email");
        user.setId(id);
        return user;
    }

    private Question createQuestion(long id) {
        Question question = new Question("title" + id, "aaaa" + id);
        question.setId(id);
        return question;
    }
}