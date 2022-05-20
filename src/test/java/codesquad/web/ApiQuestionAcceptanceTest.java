package codesquad.web;

import codesquad.HtmlFormDataBuilder;
import codesquad.domain.Question;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
    public void create_unAuthorized() {
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
        Question question = findByQuestionId(1L);
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodeForm()
                                                                             .addParameter("title", "title 수정")
                                                                             .addParameter("contents", "contents 수정")
                                                                             .build();
        //when
        ResponseEntity<String> response = basicAuthTemplate().exchange("/api/questions/1", HttpMethod.PUT, request, String.class);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(question.getTitle()).isEqualTo("title 수정");
        assertThat(question.getContents()).isEqualTo("contents 수정");
    }

    public Question createQuestion() {
        return new Question("title1", "contents");
    }
}