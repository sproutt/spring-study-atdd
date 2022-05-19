
package codesquad.web;

import codesquad.domain.Question;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.*;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void read() {
        //given
        Question question = findByQuestionId(1L);
        //when
        ResponseEntity<String> response = template().getForEntity("/api/questions" + question.getId(), String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("국내에서 Ruby on Rails와 Play가 활성화되기 힘든 이유는 뭘까?");
    }

    public Question createQuestion() {
        return new Question("title1", "contents");
    }
}
