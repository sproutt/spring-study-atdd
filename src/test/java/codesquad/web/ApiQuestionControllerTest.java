package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.domain.UserTest;
import codesquad.service.QnaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiQuestionControllerTest {
    public static final Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);

    @Mock
    private QnaService qnaService;

    @InjectMocks
    private ApiQuestionController apiQuestionController;

    private MockMvc mockMvc;

    private User user;

    private Question question;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(apiQuestionController)
                                 .build();
        user = UserTest.JAVAJIGI;
        question = createQuestion(1L);
    }

    @Test
    public void create() throws Exception {
        //given
        String questionData = objectMapper.writeValueAsString(question);

        log.debug("데이터 ={}", questionData);
        log.debug("테스트 질문 ={}", question);

        //when
        when(qnaService.create(any(), any())).thenReturn(question);

        //then
        mockMvc.perform(post("/api/questions")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(questionData))
               .andExpect(status().isOk())
               .andExpect(content().string(questionData))
               .andDo(print());
    }

    @Test
    public void read() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(question);


        //when
        when(qnaService.findById(1L)).thenReturn(question);

        //then
        MvcResult result = mockMvc.perform(get("/api/questions/1")
                                                      .contentType(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andDo(print())
                                     .andReturn();

        String responseBodyData = result.getResponse()
                                        .getContentAsString();

        log.debug("response body data = {}", responseBodyData);

        Assertions.assertThat(responseBodyData)
                  .isEqualTo(data);
    }

    @Test
    public void update() throws Exception {
        //given
        Question updatedQuestion = new Question("title 수정", "contents 수정");

        String data = objectMapper.writeValueAsString(updatedQuestion);

        //when
        when(qnaService.update(any(), eq(1L), eq(updatedQuestion)))
                .thenReturn(updatedQuestion);

        //then
        mockMvc.perform(put("/api/questions/1")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(data))
               .andExpect(status().isFound())
               .andExpect(redirectedUrl("/api/questions/1"))
               .andDo((print()));
    }


    private Question createQuestion(long id) {
        Question question = new Question("title1", "contents1");
        question.setId(id);
        return question;
    }
}