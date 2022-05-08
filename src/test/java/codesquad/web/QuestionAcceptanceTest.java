package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.helper.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.*;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void create_form() throws Exception{
        //when
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);

        log.info("body = {}", response.getBody());

        //then
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    @DisplayName("질문 생성")
    void create() throws Exception {
        //given
        HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
        htmlFormDataBuilder.addParameter("title", "title");
        htmlFormDataBuilder.addParameter("contents", "aaaa");
        htmlFormDataBuilder.addParameter("userId", "userId");
        htmlFormDataBuilder.addParameter("password", "password");
        htmlFormDataBuilder.addParameter("name", "자바지기");
        htmlFormDataBuilder.addParameter("email", "javajigi@slipp.net");

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
    @DisplayName("질문 목록 조회")
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
    @DisplayName("질문 단건 조회")
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
    @DisplayName("질문 수정 폼 요청")
    void update_form() throws Exception {
        //given
        Question savedQuestion = questionRepository.findById(1L).get();

        //when
        ResponseEntity<String> response = template().getForEntity(
                savedQuestion.generateUrl() + "/updateForm", String.class);

        log.info("body = {}", response.getBody());

        //then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(OK),
                () -> assertThat(response.getBody()).contains(savedQuestion.getContents()),
                () -> assertThat(response.getBody()).contains(savedQuestion.getTitle())
        );
    }
}
