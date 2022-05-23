package codesquad.web;

import codesquad.domain.Answer;
import org.junit.Before;
import org.junit.Test;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private static final String DEFAULT_QUESTION_URL = "/api/questions/1/answers";

    @Test
    public void create() {
        String contents = "나는 아름다운 나비";
        String location = createResource(DEFAULT_QUESTION_URL, contents);

        Answer dbAnswer = getResource(location, Answer.class);
        assertThat(dbAnswer).isNotNull();
    }
}
