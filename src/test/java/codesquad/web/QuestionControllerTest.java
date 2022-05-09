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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private QuestionController questionController;

    @Mock
    private QnaService qnaService;

    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(questionController).build();
    }

    @Test
    @DisplayName("질문 생성 폼 요청")
    void create_form() throws Exception {
        mockMvc.perform(get("/questions/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("/qna/form"));
    }

    @Test
    @DisplayName("질문 생성")
    void create() throws Exception {
        //given
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("title", "title");
        map.add("contents", "aaaa");
        map.add("userId", "userId");
        map.add("password", "password");
        map.add("name", "name");
        map.add("email", "email");

        //when
        Question question = createQuestion();

        when(qnaService.create(createUser(), question)).thenReturn(question);

        //then
        mockMvc.perform(post("/questions")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(map))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("질문 목록 조회")
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
    @DisplayName("질문 단건 조회")
    void show_question() throws Exception {
        //given
        Question question = new Question("국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?", "aaa");
        question.setId(1L);

        //when
        when(qnaService.findById(1L)).thenReturn(Optional.of(question));

        //then
        mockMvc.perform(get(question.generateUrl())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(view().name("/qna/show"))
                .andDo(print());
    }

    @Test
    @DisplayName("질문 수정 폼 요청")
    void update_form() throws Exception {
        //given
        Question question = createQuestion();
        question.setId(1L);

        //when
        when(qnaService.findById(1L)).thenReturn(Optional.of(question));

        //then
        mockMvc.perform(get(question.generateUrl() + "/updateForm")
                        .param("id", String.valueOf(question.getId())))
                .andExpect(status().isOk())
                .andExpect(view().name("/qna/updateForm"))
                .andExpect(model().attributeExists("question"))
                .andDo(print());
    }

    @Test
    @DisplayName("질문 수정 요청 시 UnAuthenticationException이 발생하면 홈으로 리다이렉트 시킨다")
    void update_fail() throws Exception {
        //when
        when(qnaService.update(anyObject(), anyLong(), anyObject())).thenThrow(UnAuthenticationException.class);

        //then
        mockMvc.perform(put("/questions/1"))
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    @Test
    @DisplayName("질문 수정 요청 시 성공하면 수정된 질문으로 이동한다")
    void update_success() throws Exception {
        //given
        User user = createUser();
        user.setId(1L);

        Question question = createQuestion();
        question.setId(1L);
        question.writeBy(user);

        QuestionDto questionDto = question.toDto();
        questionDto.setContents("updateContents");
        questionDto.setTitle("updateTitle");

        //when
        when(qnaService.update(user, 1L, questionDto)).thenReturn(question);

        //then
        mockMvc.perform(put("/questions/1")
                        .param("title", "updateTitle")
                        .param("contents", "updateContents")
                        .param("writer", user.getUserId())
                        .param("userId", "a")
                        .param("password", "a")
                        .param("name", "a")
                        .param("email", "a")
                        .param("id", String.valueOf(question.getId()))
                )
                .andExpect(redirectedUrl(question.generateUrl()))
                .andExpect(model().attributeExists("question"))
                .andDo(print());
    }

    @Test
    @DisplayName("질문 삭제 요청 시 UnAuthenticationException이 발생하면 삭제를 시도한 질문으로 리다이렉트한다")
    void delete_fail() throws Exception{
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
        //given
        User user = createUser();
        user.setId(1L);
        Question question = createQuestion();
        question.setId(1L);

        //when
        doNothing()
                .when(qnaService)
                .deleteQuestion(user, question.getId());

        //then
        mockMvc.perform(delete(question.generateUrl()))
                .andExpect(redirectedUrl("/"))
                .andDo(print());
    }

    private User createUser() {
        return new User("a", "a", "a", "a");
    }

    private Question createQuestion() {
        return new Question("title", "contents");
    }


}