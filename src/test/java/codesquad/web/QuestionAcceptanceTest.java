package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.helper.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.*;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;

    private HtmlFormDataBuilder htmlFormDataBuilder;

    @BeforeEach
    void htmlFormTestDataBuild() {
        htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm()
                                                 .addParameter("userId", defaultUser().getUserId())
                                                 .addParameter("password", defaultUser().getPassword())
                                                 .addParameter("title", "title")
                                                 .addParameter("contents", "aaaa");
    }

    @Test
    @DisplayName("질문 생성 폼을 요청할 수 있다")
    void create_form() throws Exception {
        //when
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);

        log.info("body = {}", response.getBody());

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    @DisplayName("로그인 한 유저는 새로운 질문을 생성할 수 있다")
    void create() throws Exception {
        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/questions", htmlFormDataBuilder.build(), String.class);

        log.info("response = {}", response);

        //then
        assertAll(
                () -> assertEquals(response.getStatusCode(), HttpStatus.FOUND),
                () -> assertThat(response.getHeaders().getLocation().getPath()).startsWith("/"),
                () -> assertThat(questionRepository.findById(3L).isPresent())
        );
    }

    @Test
    @DisplayName("누구든지 모든 질문 목록을 조회할 수 있다")
    void list() throws Exception {
        //given
        ResponseEntity<String> response = template().getForEntity("/", String.class);

        //then
        assertAll(
                () -> assertEquals(response.getStatusCode(), HttpStatus.OK),
                () -> assertThat(response.getBody()).contains("국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?"),
                () -> assertThat(response.getBody()).contains("runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?")
        );
    }

    @Test
    @DisplayName("누구든지 질문 단건 조회할 수 있다")
    void show_question() throws Exception {
        //given
        Question savedQuestion = questionRepository.findById(1L).get();

        //when
        ResponseEntity<String> response = template().getForEntity(savedQuestion.generateUrl(), String.class);

        //then
        assertAll(
                () -> assertEquals(response.getStatusCode(), HttpStatus.OK),
                () -> assertThat(response.getBody()).contains(savedQuestion.getContents()),
                () -> assertThat(response.getBody()).contains(savedQuestion.getTitle())
        );
    }

    @Test
    @DisplayName("질문 작성자일 경우 질문을 수정할 폼을 요청할 수 있다")
    void update_form() throws Exception {
        //given
        Question savedQuestion = questionRepository.findById(1L).get();

        //when
        ResponseEntity<String> response = basicAuthTemplate().getForEntity(
                savedQuestion.generateUrl() + "/updateForm", String.class);

        log.info("body = {}", response.getBody());

        //then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(OK),
                () -> assertThat(response.getBody()).contains(savedQuestion.getContents()),
                () -> assertThat(response.getBody()).contains(savedQuestion.getTitle())
        );
    }

    @Test
    @DisplayName("질문 작성자가 아니거나 로그인을 하지 않을 경우 질문 수정하지 못한다")
    void update_fail() throws Exception {
        //given
        Question savedQuestion = questionRepository.findById(2L).get();
        htmlFormDataBuilder.addParameter("_method", "put");
        htmlFormDataBuilder.addParameter("writer", defaultUser().getUserId());

        //when
        ResponseEntity<String> response = template().postForEntity(savedQuestion.generateUrl(), htmlFormDataBuilder.build(), String.class);

        log.info("response = {}", response);

        //then
        assertEquals(response.getStatusCode(), FORBIDDEN);
    }


    @Test
    @DisplayName("질문 작성자는 제목과 내용을 수정할 수 있다")
    void update_success() throws Exception {
        //given
        Question savedQuestion = questionRepository.findById(1L).get();
        htmlFormDataBuilder.addParameter("writer", defaultUser().getUserId());
        htmlFormDataBuilder.addParameter("_method", "put");

        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(savedQuestion.generateUrl(), htmlFormDataBuilder.build(), String.class);

        log.info("response = {}", response);

        //put 요청을 할 때, 수정된 내용이 있으면 body로 반환한다고 알고 있다
        //response.getBody를 하면 null 값이 나오게 됨

        //then
        assertAll(
                () -> assertEquals(response.getStatusCode(), FOUND),
                () -> assertThat(response.getHeaders().getLocation().getPath()).isEqualTo(savedQuestion.generateUrl())
        );
    }

    @Test
    @DisplayName("질문 작성자가 아니거나 로그인을 하지 않을 경우 질문 삭제를 하지 못한다")
    void delete_fail() throws Exception {
        //given
        Question savedQuestion = questionRepository.findById(2L).get();
        htmlFormDataBuilder.addParameter("_method", "DELETE");

        //when
        ResponseEntity<String> response = template().postForEntity(savedQuestion.generateUrl(), htmlFormDataBuilder.build(), String.class);

        log.info("response = {}", response);

        //then
        assertEquals(response.getStatusCode(), FORBIDDEN);
    }

    @Test
    @DisplayName("질문 작성자는 질문을 삭제할 수 있다")
    void delete_success() throws Exception {
        //given
        Question savedQuestion = questionRepository.findById(1L).get();
        htmlFormDataBuilder.addParameter("_method", "DELETE");

        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(savedQuestion.generateUrl(), htmlFormDataBuilder.build(), String.class);

        log.info("response = {}", response);

        //then
        assertAll(
                () -> assertEquals(response.getStatusCode(), FOUND),
                () -> assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/")
        );
    }
}