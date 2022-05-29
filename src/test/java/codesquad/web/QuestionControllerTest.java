package codesquad.web;

import codesquad.domain.User;
import codesquad.domain.UserTest;
import codesquad.service.QuestionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class QuestionControllerTest {
    @InjectMocks
    QuestionController questionController;

    @Mock
    QuestionService questionService;

    private User user;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        user = UserTest.JAVAJIGI;
        mockMvc = MockMvcBuilders.standaloneSetup(questionController)
                                 .build();
    }

    @Test
    public void form() throws Exception {
        mockMvc.perform(get("/questions/form")
                       .param("userId", user.getUserId())
                       .param("password", user.getPassword())
                       .param("name", user.getName())
                       .param("email", user.getEmail()))
               .andExpect(status().isOk())
               .andExpect(forwardedUrl("qna/form"));
    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(post("/questions")
                       .param("userId", user.getUserId())
                       .param("password", user.getPassword())
                       .param("name", user.getName())
                       .param("email", user.getEmail())
                       .param("title", "title1")
                       .param("contents", "contents1"))
               .andExpect(status().isFound());
    }

    @Test
    public void remove() throws Exception {
        mockMvc.perform(delete("/questions/{id}", 1)
                       .param("userId", user.getUserId())
                       .param("password", user.getPassword())
                       .param("name", user.getName())
                       .param("email", user.getEmail()))
               .andExpect(status().isFound());
    }

    @Test
    public void show() throws Exception {
        mockMvc.perform(get("/questions/{id}", 1)
                       .param("userId", user.getUserId())
                       .param("password", user.getPassword())
                       .param("name", user.getName())
                       .param("email", user.getEmail()))
               .andExpect(status().isOk())
               .andExpect(forwardedUrl("qna/show"));
    }

    @Test
    public void updateForm() throws Exception {
        mockMvc.perform(get("/questions/updateForm")
                       .param("userId", user.getUserId())
                       .param("password", user.getPassword())
                       .param("name", user.getName())
                       .param("email", user.getEmail()))
               .andExpect(status().isOk())
               .andExpect(forwardedUrl("qna/updateForm"));
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(put("/questions/{id}", 1)
                       .param("userId", user.getUserId())
                       .param("password", user.getPassword())
                       .param("name", user.getName())
                       .param("email", user.getEmail())
                       .param("title", "title1")
                       .param("contents", "contents1"))
               .andExpect(status().isFound());
    }
}