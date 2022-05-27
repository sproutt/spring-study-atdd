package codesquad.web;

import codesquad.domain.Question;
import org.junit.Test;
import org.springframework.http.*;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.*;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    @Test
    public void create_authorized() {
        //given
        Question question = createQuestion();
        //when
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", question, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(findByQuestionId(3L)).isNotNull();
    }

    @Test
    public void create_unauthorized() {
        //given
        Question question = createQuestion();
        //when
        ResponseEntity<String> response = template().postForEntity("/api/questions", question, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

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

    @Test
    public void update_authorized() {
        //given
        Question question = new Question("title1 수정", "contents1 수정");
        HttpEntity<Question> request = new HttpEntity<>(question);
        //when
        ResponseEntity<String> response = basicAuthTemplate().exchange("/api/questions/1", HttpMethod.PUT, request, String.class);
        String responseBodyData = response.getBody();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBodyData).contains("title1 수정");
        assertThat(responseBodyData).contains("contents1 수정");

    }

    @Test
    public void update_unauthorized() {
        //given
        Question question = new Question("title1 수정", "contents1 수정");
        HttpEntity<Question> request = new HttpEntity<>(question);
        //when
        ResponseEntity<String> response = template().exchange("/api/questions/1", HttpMethod.PUT, request, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_authorized() {
        //given
        Question savedQuestion = findByQuestionId(1L);
        HttpEntity<Question> request = new HttpEntity<>(savedQuestion);

        //when
        ResponseEntity<String> response = basicAuthTemplate().exchange("/api/questions/1", HttpMethod.DELETE, request, String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(findByQuestionId(1L).isDeleted()).isEqualTo(true);
    }

    @Test
    public void delete_unauthorized() {
        //given
        Question savedQuestion = findByQuestionId(1L);
        HttpEntity<Question> request = new HttpEntity<>(savedQuestion);
        //when
        ResponseEntity<String> response = template().exchange("/api/questions/1", HttpMethod.DELETE, request, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(findByQuestionId(1L).isDeleted()).isEqualTo(false);
    }



    public Question createQuestion() {
        return new Question("title1", "contents");
    }
}