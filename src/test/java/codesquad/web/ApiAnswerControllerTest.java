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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        when(qnaService.addAnswer(any(), anyLong(), any())).thenReturn(answer);
        //then
        mockMvc.perform(post("/api/questions/1/answers")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(answer.getContents()))
               .andExpect(status().isOk())
               .andExpect(content().string(answerData))
               .andDo(print());
    }

    @Test
    public void read() throws Exception {
        //given
        //when
        when(qnaService.findByAnswerId(1L)).thenReturn(answer);

        //then
        mockMvc.perform(get("/api/questions/1/answers/1")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(status().isOk())
               .andExpect(content().string(objectMapper.writeValueAsString(answer)));
    }
    
    @Test
    public void update() throws Exception {
        //given
        String answerData = objectMapper.writeValueAsString(answer);

        //when
        when(qnaService.updateAnswer(any(), anyLong(), anyString())).thenReturn(answer);
        //then
        mockMvc.perform(put("/api/questions/1/answers/1")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(answer.getContents()))
               .andExpect(status().isOk())
               .andExpect(content().string(answerData))
               .andDo(print());
    }

    @Test
    public void delete() throws Exception {
        //given
        answer.delete();

        //when
        when(qnaService.deleteAnswer(any(), anyLong())).thenReturn(answer);

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/questions/1/answers/1")
                                              .contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.deleted").value(true));
    }
}
