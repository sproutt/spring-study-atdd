package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void create_authorized() {
        //given
        Answer answer = createAnswer();

        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions/1/answers", answer, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(answerRepository.findById(3L).get()).isNotNull();
    }

    @Test
    public void create_unauthorized() {
        //given
        Answer answer = createAnswer();

        //when
        ResponseEntity<String> response = template().postForEntity("/api/questions/1/answers", answer, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void read() {
        //given

        //when
        ResponseEntity<String> response = template().getForEntity("/api/questions/1/answers/1", String.class);

        //then
        Assertions.assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()
                                         .contains(answerRepository.findById(1L)
                                                                   .get()
                                                                   .getContents())));
    }

    @Test
    public void update_authorized(){
        //given
        HttpEntity<String> request = new HttpEntity<>("수정된 내용");

        //when
        ResponseEntity<String> response = basicAuthTemplate().exchange("/api/questions/1/answers/1", HttpMethod.PUT, request, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(answerRepository.findById(1L)
                                   .get()
                                   .getContents()).isEqualTo("수정된 내용");
    }

    @Test
    public void update_unauthorized() {
        //given
        HttpEntity<String> request = new HttpEntity<>("수정된 내용");

        //when
        ResponseEntity<String> response = template().exchange("/api/questions/1/answers/1", HttpMethod.PUT, request, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private Answer createAnswer() {
        Answer answer = new Answer(defaultUser(), "contents1");
        answer.toQuestion(findByQuestionId(1L));
        return answer;
    }
}
