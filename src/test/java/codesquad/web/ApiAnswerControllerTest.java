package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.domain.UserTest;
import codesquad.service.QnaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ApiAnswerControllerTest {

    @Mock
    private QnaService qnaService;

    @InjectMocks
    private ApiAnswerController apiAnswerController;

    private MockMvc mockMvc;

    private User user;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Answer answer;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(apiAnswerController)
                                 .build();
        user = UserTest.JAVAJIGI;
        answer = new Answer(user, "contents1");
    }

    @Test
    public void create() throws Exception {
        //given
        String answerData = objectMapper.writeValueAsString(answer);

        //when
        when(qnaService.addAnswer(user, 1L, "contents1")).thenReturn(answer);
        //then
        mockMvc.perform(post("/api/answers")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(answer.getContents()))
               .andExpect(status().isOk())
               .andExpect(content().string(answerData))
               .andDo(print());
    }
}
