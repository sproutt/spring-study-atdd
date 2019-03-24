package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static codesquad.domain.UserTest.newUser;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    @Test
    public void post() {
        String contents = "contents1";

        ResponseEntity<Void> response =
                basicAuthTemplate(defaultUser()).postForEntity("/api" + defaultQuestion().generateUrl() + "/answers", contents, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void post_no_login() {
        String contents = "contents2";

        ResponseEntity<Void> response =
                template().postForEntity("/api" + defaultQuestion().generateUrl() + "/answers", contents, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete() {
        String contents = "contents3";
        User user = defaultUser();

        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(user)
                        .exchange(createResource("/api" + defaultQuestion().generateUrl() + "/answers", contents, user), HttpMethod.DELETE,
                                createHttpEntity(null), Answer.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().isDeleted()).isTrue();
    }

    @Test
    public void delete_다른_사람() {
        String contents = "contents4";
        User user = newUser("testuser2");
        createResource("/api/users", user);

        ResponseEntity<Answer> responseEntity =
                basicAuthTemplate(defaultUser())
                        .exchange(createResource("/api" + defaultQuestion().generateUrl() + "/answers", contents, user), HttpMethod.DELETE,
                                createHttpEntity(null), Answer.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
    }
}
