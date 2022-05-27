package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private static final String DEFAULT_QUESTION_URL = "/api/questions/1/answers";

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    public void create() {
        String contents = "나는 아름다운 나비";
        String location = createResource(DEFAULT_QUESTION_URL, contents);

        Answer dbAnswer = answerRepository.findById(3L).get();
        assertThat(dbAnswer).isNotNull();
    }

    @Test
    public void update() {
        String contents = "나는 아름다운 나비";
        String location = createResource(DEFAULT_QUESTION_URL, contents);

        String updatedContents = "나는 꿀을 빠는 꿀벌";
        ResponseEntity<Answer> response = basicAuthTemplate().exchange(location, HttpMethod.PUT, createHttpEntity(updatedContents), Answer.class);

        assertThat(response.getBody().getContents()).isEqualTo(updatedContents);
    }

    @Test
    public void update_다른_사람() {
        String contents = "나는 아름다운 나비";
        User otherUser = newUser(2L);
        String location = createResource(DEFAULT_QUESTION_URL, contents);

        String updatedContents = "나는 꿀을 빠는 꿀벌";
        ResponseEntity<Answer> response = basicAuthTemplate(otherUser).exchange(location, HttpMethod.PUT, createHttpEntity(updatedContents), Answer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() {
        String contents = "나는 아름다운 나비";
        String location = createResource(DEFAULT_QUESTION_URL, contents);

        basicAuthTemplate().delete(location);

        Answer dbAnswer = answerRepository.findById(3L).get();
        assertThat(dbAnswer.isDeleted()).isTrue();
    }

    @Test
    public void delete_다른_사람() {
        String contents = "나는 아름다운 나비";
        User otherUser = newUser(2L);
        String location = createResource(DEFAULT_QUESTION_URL, contents);

        basicAuthTemplate(otherUser).delete(location);

        Answer dbAnswer = answerRepository.findById(3L).get();
        assertThat(dbAnswer.isDeleted()).isFalse();
    }
}
