package codesquad.web;

import codesquad.NullEntityException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.post.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;

    private Question defaultQuestion;

    final private long TESTQUESTIONID = 1;

    @Before
    public void setUp() {
        defaultQuestion = questionRepository.findById(TESTQUESTIONID).orElseThrow(() -> new NullEntityException());
    }

    @Test
    public void create() throws Exception {
        String questionTitle = "test";
        User loginUser = defaultUser();

        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", questionTitle)
                .addParameter("contents", "testContents")
                .build();

        ResponseEntity<String> response = basicAuthTemplate(loginUser).postForEntity("/questions", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findByTitle(questionTitle).isPresent()).isTrue();
    }

    @Test
    public void createForm_no_login() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void createForm_login() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).getForEntity("/questions/form", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void list() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(questionRepository.findById((long)2).orElseThrow(()->new NullEntityException()).getTitle());
    }

    @Test
    public void show() throws Exception {
        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d", defaultQuestion.getId()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(defaultQuestion.getContents());
    }

    @Test
    public void updateForm_no_login() throws Exception {
        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d/form", defaultQuestion.getId()),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    public void updateForm_login() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser())
                .getForEntity(String.format("/questions/%d/form", defaultQuestion.getId()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(defaultQuestion.getTitle());
    }

    @Test
    public void update() throws Exception {
        String changeTitle = "changeTitle";
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", changeTitle)
                .addParameter("contents", defaultQuestion.getContents())
                .build();

        basicAuthTemplate(defaultUser()).put(String.format("/questions/%d", defaultQuestion.getId()), request, String.class);

        assertThat(questionRepository.findById(defaultQuestion.getId())
                .orElseThrow(() -> new NullEntityException())
                .getTitle()).isEqualTo(changeTitle);
    }


    @Test
    public void delete() throws Exception {
        basicAuthTemplate(defaultUser()).delete(String.format("/questions/%d", defaultQuestion.getId()), String.class);

        assertThat(questionRepository.findById(defaultQuestion.getId())
                .orElseThrow(() -> new NullEntityException())
                .isDeleted()).isEqualTo(true);
    }


}
