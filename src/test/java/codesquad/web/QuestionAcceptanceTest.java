package codesquad.web;

import codesquad.domain.QuestionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.*;
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
}
