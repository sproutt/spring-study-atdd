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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private User createUser() {
        return new User("a", "a", "a", "a");
    }

    private Question createQuestion() {
        return new Question("title", "contents");
    }
}