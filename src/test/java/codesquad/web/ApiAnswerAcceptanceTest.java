package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
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
        ResponseEntity<String> response = template().getForEntity("/api/questions/1/answers", String.class);

        //then
        Assertions.assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()
                                         .contains(answerRepository.findById(1L)
                                                                   .get()
                                                                   .getContents())));
    }

    private Answer createAnswer() {
        Answer answer = new Answer(defaultUser(), "contents1");
        answer.toQuestion(findByQuestionId(1L));
        return answer;
    }
}
