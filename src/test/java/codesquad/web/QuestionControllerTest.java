package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.service.QnaService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private User createUser() {
        return new User("a", "a", "a", "a");
    }

    private Question createQuestion() {
        return new Question("title", "contents");
    }
}