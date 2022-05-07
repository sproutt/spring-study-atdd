package codesquad.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(QuestionController.class)
@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private QuestionController questionController;

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
}