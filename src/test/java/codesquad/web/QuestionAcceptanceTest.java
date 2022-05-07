package codesquad.web;

import codesquad.HtmlFormDataBuilder;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;


import static org.assertj.core.api.Assertions.assertThat;

public class QuestionAcceptanceTest extends AcceptanceTest {
    public static final Logger log = LoggerFactory.getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;

    private Question question;

    @Before
    public void before() {
        Question question = new Question("before() Title", "before() Contents");
        question.setWriter(defaultUser());
        this.question = questionRepository.save(question);
    }

    @Test
    @DisplayName("로그인 하지 않고 질문 작성 폼 보여주기 요청이 들어왔을때 보여주지 않는지 테스트")
    public void createForm_unauthorized() {
        //given
        //when
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        //then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("로그인 하고 질문 작성 폼 보여주기 요청이 들어왔을때 잘 보여주는지 테스트")
    public void createForm_authorized() {
        //given
        //when
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/questions/form", String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("로그인 하지 않았을 때 질문이 생성되지 않는지를 테스트")
    public void create_unauthorized() {
        //given
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        htmlFormDataBuilder.addParameter("title", "title1");
        htmlFormDataBuilder.addParameter("contents", "contents1");
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        //when
        ResponseEntity<String> response = template().postForEntity("/questions", request, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("로그인 했을 때 질문이 잘 생성되는지 테스트")
    public void create_authorized() {
        //given
        int size = questionRepository.findAll()
                                     .size();
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        htmlFormDataBuilder.addParameter("title", "title1");
        htmlFormDataBuilder.addParameter("contents", "contents1");
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/questions", request, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findAll().size()).isEqualTo(size+1);
    }

    @Test
    @DisplayName("로그인 하지 않고 질문을 삭제하려고 했을 때 삭제가 안되는지 테스트")
    public void delete_unauthorized() {
        //given
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        //when
        ResponseEntity<String> response = template().exchange("/questions/1", HttpMethod.DELETE, request, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("로그인 하고 질문을 삭제하려고 했을 때 잘 삭제 되는지 테스트")
    public void delete_authorized() {
        //given
        int size = questionRepository.findAll()
                                     .size();
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm();
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        //when
        ResponseEntity<String> response = basicAuthTemplate().exchange("/questions/1", HttpMethod.DELETE, request, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findAll()
                                     .size()).isEqualTo(size - 1);
    }

    @Test
    @DisplayName("로그인 하지 않고 작성된 질문을 읽으려고 했을 때 읽어지지 않는지 테스트")
    public void read_unauthorized() {
        //given
        Long id = 1L;
        //when
        ResponseEntity<String> response = template().getForEntity("/questions/" + id, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("로그인 하고 작성된 질문을 읽으려고 했을 때 잘 읽어지는지 테스트")
    public void read_authorized() {
        //given
        //when
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/questions/" + this.question.getId(), String.class);
        //then
        log.debug("response.getBody() = {}", response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(this.question.getContents());
    }

    @Test
    @DisplayName("로그인 하지 않고 수정 폼을 열려고 할 때 열리지 않는지 테스트")
    public void updateForm_unauthorized() {
        //given
        //when
        ResponseEntity<String> response = template().getForEntity("/questions/updateForm", String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("로그인 하고 수정 폼을 열려고 했을 때 잘 열리는지 테스트")
    public void updateForm_authorized() {
        //given
        //when
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/questions/updateForm", String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("로그인 하지 않고 수정 요청을 보냈을 때 수정이 되지 않는지 테스트")
    public void update_unauthorized() {
        //given
        Long id = this.question.getId();
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm()
                                                                     .addParameter("title", "수정할 제목")
                                                                     .addParameter("contents", "수정된 내용");
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        //when
        ResponseEntity<String> response = template().exchange("/questions/" + id, HttpMethod.PUT, request, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("로그인 하고 수정 요청을 보냈을 때 수정이 잘 되는지 테스트")
    public void update_authorized() {
        //given
        Long id = this.question.getId();
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodeForm()
                                                                     .addParameter("title", "수정할 제목")
                                                                     .addParameter("contents", "수정된 내용");
        HttpEntity<MultiValueMap<String, Object>> request = htmlFormDataBuilder.build();
        //when

        ResponseEntity<String> response = basicAuthTemplate().exchange("/questions/" + id, HttpMethod.PUT, request, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }
}