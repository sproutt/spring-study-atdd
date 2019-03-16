package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionAcceptanceTest extends AcceptanceTest {
    @Autowired
    private QuestionRepository questionRepository;

    private HttpEntity<MultiValueMap<String, Object>> request;
    private User testUser;
    private Question testQuestion;

    @Before
    public void setUp() throws Exception {
        testUser = new User("testUser", "test", "userName", "test@gmail.com");
        testQuestion = new Question("Title", "this is test");
        testQuestion.writeBy(testUser);

        request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", testQuestion.getTitle())
                .addParameter("contents", testQuestion.getContents())
                .addParameter("writer", testQuestion.getWriter().getName())
                .build();
    }

    @Test
    public void create() {
        ResponseEntity<String> response = template().postForEntity("/questions", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findAll().stream()
                .filter(question -> question.getWriter().equals(testUser.getName())));
        assertThat(response.getHeaders().getLocation().getPath().startsWith("/questions"));
    }

    @Test
    public void show() {
        ResponseEntity<String> response = template().getForEntity(testQuestion.generateUrl(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(testQuestion.getContents());

    }

    @Test
    public void updateForm_login() {
        ResponseEntity<String> response = basicAuthTemplate(testUser)
                .getForEntity(String.format(testQuestion.generateUrl()), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().contains(testQuestion.getWriter().getName()));
    }

    @Test
    public void updateForm_no_login() throws Exception {
        ResponseEntity<String> response = update(template());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<String> update(TestRestTemplate template) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("_method", "put")
                .addParameter("title", "test")
                .addParameter("contents", "this is update test")
                .build();

        return template.postForEntity(testQuestion.generateUrl(), request, String.class);
    }

    @Test
    public void update() throws Exception {
        ResponseEntity<String> response = update(basicAuthTemplate(testUser));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().getPath()).startsWith("/questions");
    }
}
