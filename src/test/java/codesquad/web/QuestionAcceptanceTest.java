package codesquad.web;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.HtmlFormDataBuilder;
import codesquad.domain.QuestionRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

public class QuestionAcceptanceTest extends AcceptanceTest {

    private static final Logger log = LoggerFactory.getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void createForm_login() throws Exception{
        ResponseEntity<String> response = basicAuthTemplate().getForEntity(String.format("/questions/form"), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void createForm_no_login() throws Exception {
        ResponseEntity<String> response = template().getForEntity(String.format("/questions/form"), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void create_login() throws Exception {
        ResponseEntity<String> responseEntity = create(basicAuthTemplate());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findAll().size()).isNotZero();

        assertThat(responseEntity.getHeaders().getLocation().getPath()).startsWith("/");
    }

    @Test
    public void create_no_login() throws Exception {
        ResponseEntity<String> responseEntity = create(template());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<String> create(TestRestTemplate template) {
        HttpEntity<MultiValueMap<String, Object>> requestEntity = HtmlFormDataBuilder.urlEncodedForm()
                                                                      .addParameter("title", "title")
                                                                      .addParameter("contents", "contents")
                                                                      .build();

        return template.postForEntity("/questions", requestEntity, String.class);
    }

    @Test
    public void updateForm_login() throws Exception {
        ResponseEntity<String> responseEntity = updateForm(basicAuthTemplate());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains(defaultQuestion().getTitle());
        assertThat(responseEntity.getBody()).contains(defaultQuestion().getContents());
    }

    @Test
    public void updateForm_no_login() throws Exception {
        ResponseEntity<String> responseEntity = updateForm(template());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<String> updateForm(TestRestTemplate template) {
        return template.getForEntity(String.format("/questions/%d/form", defaultQuestion().getId()), String.class);
    }


    @Test
    public void update_no_login() throws Exception {
        ResponseEntity<String> responseEntity = update(template());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update_login() throws Exception {
        ResponseEntity<String> responseEntity = update(basicAuthTemplate());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(responseEntity.getHeaders().getLocation().getPath()).startsWith("/");
    }

    private ResponseEntity<String> update(TestRestTemplate template) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                .addParameter("_method", "put")
                                                                .addParameter("title", "test_title")
                                                                .addParameter("contents", "test_contents")
                                                                .build();

        return template.postForEntity(String.format("/questions/%d", defaultUser().getId()), request, String.class);
    }

    @Test
    public void delete_no_login() throws Exception {
        ResponseEntity<String> responseEntity = delete(template());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void delete_login() throws Exception {
        ResponseEntity<String> responseEntity = delete(basicAuthTemplate());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(responseEntity.getHeaders().getLocation().getPath()).startsWith("/");
    }

    private ResponseEntity<String> delete(TestRestTemplate template) throws Exception {
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                                                                .addParameter("_method", "delete")
                                                                .build();

        return template.postForEntity(String.format("/questions/%d", defaultQuestion().getId()), request, String.class);
    }

    @Test
    public void show() throws Exception{
        ResponseEntity<String> responseEntity = template().getForEntity(String.format("/questions/%d", defaultQuestion().getId()), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains(defaultQuestion().getContents());
        assertThat(responseEntity.getBody()).contains(defaultQuestion().getTitle());
    }
}
