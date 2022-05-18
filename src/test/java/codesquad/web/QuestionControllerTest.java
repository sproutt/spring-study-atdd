package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.service.QnaService;
import codesquad.web.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(QuestionController.class)
@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private QuestionController questionController;

    @Mock
    private QnaService qnaService;

    private User dummyUser;
    private Question dummyQuestion;

    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(questionController).build();
        dummyUser = createUser(1L);
        dummyQuestion = createQuestion(1L);
    }

    @Test
    @DisplayName("질문 생성 폼 요청을 할 경우 생성 폼으로 이동한다")
    void create_form() throws Exception {
        mockMvc.perform(get("/questions/form"))
               .andExpect(status().isOk())
               .andExpect(view().name("/qna/form"));
    }

    @Test
    @DisplayName("질문 생성 요청이 성공하면 홈으로 리다이렉트한다")
    void create() throws Exception {
        //given
        Question question = createQuestion(1L);
        User user = createUser(1L);

        //when
        when(qnaService.create(user, question)).thenReturn(question);

        //then
        mockMvc.perform(post("/questions")
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                       .param("id", String.valueOf(question.getId()))
                       .param("title", question.getTitle())
                       .param("contents", question.getContents())
                       .param("userId", user.getUserId())
                       .param("password", user.getPassword())
                       .param("name", user.getName())
                       .param("email", user.getEmail()))
               .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("모든 질문 목록 조회 요청이 성공하면 홈으로 이동한다")
    void list() throws Exception {
        //when
        when(qnaService.findAll()).thenReturn(
                Arrays.asList(
                        new Question("국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?", "aaa"),
                        new Question("runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?", "aaa")));

        //then
        mockMvc.perform(get("/")
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(view().name("/home"))
               .andDo(print());
    }

    @Test
    @DisplayName("질문 단건 조회 요청이 성공하면 질문 상세보기 폼으로 이동한다")
    void show_question() throws Exception {
        //when
        when(qnaService.findById(1L)).thenReturn(Optional.of(dummyQuestion));

        //then
        mockMvc.perform(get(dummyQuestion.generateUrl())
                       .contentType(MediaType.APPLICATION_FORM_URLENCODED))
               .andExpect(view().name("/qna/show"))
               .andDo(print());
    }

    @Test
    @DisplayName("질문 수정 폼 요청이 성공하면 수정 전의 정보들이 담긴 수정 폼으로 이동한다")
    void update_form() throws Exception {
        //when
        when(qnaService.findById(1L)).thenReturn(Optional.of(dummyQuestion));

        //then
        mockMvc.perform(get(dummyQuestion.generateUrl() + "/updateForm")
                       .param("id", String.valueOf(dummyQuestion.getId())))
               .andExpect(status().isOk())
               .andExpect(view().name("/qna/updateForm"))
               .andExpect(model().attributeExists("question"))
               .andDo(print());
    }

    @Test
    @DisplayName("질문 수정 요청 시 UnAuthenticationException이 발생하면 홈으로 리다이렉트 시킨다")
    void update_fail() throws Exception {
        //when
        when(qnaService.update(anyObject(), anyLong(), anyObject()))
                .thenThrow(UnAuthenticationException.class);

        //then
        mockMvc.perform(put("/questions/1"))
               .andExpect(redirectedUrl("/"))
               .andDo(print());
    }

    @Test
    @DisplayName("질문 수정 요청 시 성공하면 수정된 질문으로 이동한다")
    void update_success() throws Exception {
        //given
        dummyQuestion.writeBy(dummyUser);

        QuestionDto questionDto = new QuestionDto(
                dummyUser.getUserId(),
                dummyQuestion.getTitle(),
                dummyQuestion.getContents());

        questionDto.setContents("updateContents");
        questionDto.setTitle("updateTitle");

        //when
        when(qnaService.update(dummyUser, 1L, questionDto)).thenReturn(dummyQuestion);

        //then
        mockMvc.perform(put("/questions/1")
                       .param("title", "updateTitle")
                       .param("contents", "updateContents")
                       .param("writer", dummyUser.getUserId())
                       .param("userId", dummyUser.getUserId())
                       .param("password", dummyUser.getPassword())
                       .param("name", dummyUser.getName())
                       .param("email", dummyUser.getEmail())
                       .param("id", String.valueOf(dummyQuestion.getId())))
               .andExpect(redirectedUrl(dummyQuestion.generateUrl()))
               .andExpect(model().attributeExists("question"))
               .andDo(print());
    }

    @Test
    @DisplayName("질문 삭제 요청 시 UnAuthenticationException이 발생하면 삭제를 시도한 질문으로 리다이렉트한다")
    void delete_fail() throws Exception {
        //when
        doThrow(CannotDeleteException.class)
                .when(qnaService)
                .deleteQuestion(anyObject(), anyLong());

        //then
        mockMvc.perform(delete("/questions/1"))
               .andExpect(redirectedUrl("/questions/1"))
               .andDo(print());
    }

    @Test
    @DisplayName("질문 삭제 요청 시 성공하면 홈으로 리다이렉트한다")
    void delete_success() throws Exception {
        //when
        doNothing().when(qnaService).deleteQuestion(dummyUser, dummyQuestion.getId());

        //then
        mockMvc.perform(delete(dummyQuestion.generateUrl()))
               .andExpect(redirectedUrl("/"))
               .andDo(print());
    }

    private User createUser(long id) {
        User user = new User("userId", "password", "name", "email");
        user.setId(id);
        return user;
    }

    private Question createQuestion(long id) {
        Question question = new Question("title", "contents");
        question.setId(id);
        return question;
    }
}