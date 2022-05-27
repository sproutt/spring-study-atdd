package codesquad.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new QuestionController()).build();
    }

    @Test
    public void 질문_생성_폼을_반환한다() throws Exception {
        mockMvc.perform(get("/questions/form")
               .param("userId", "javajigi")
               .param("password", "test")
               .param("name", "자바지기")
               .param("email", "javajigi@slipp.net"))
               .andExpect(status().isOk())
               .andExpect(view().name("/qna/form"))
               .andDo(print());
    }
}
