package codesquad.web;

import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionCRUDAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(QuestionCRUDAcceptanceTest.class);

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void createForm_no_login() {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void list() {
        ResponseEntity<String> response = template().getForEntity("/questions", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void createFormWith_login() {
        User loginUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loginUser).getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void create() {
        int originalSize = questionRepository.findAll().size();
        User loginUser = defaultUser();
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", "테스트용 제목")
                .addParameter("content", "테스트용 본문")
                .build();

        ResponseEntity<String> response = basicAuthTemplate(loginUser)
                .postForEntity(String.format("/questions"), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findAll().size()).isEqualTo(originalSize + 1);
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/questions");
    }

    @Test
    public void create_no_login() {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void update() {
        Long originalId = 1L;
        User loginUser = defaultUser();
        String editedTitle = "테스트용 수정된 제목";
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("_method", "put")
                .addParameter("title", editedTitle)
                .addParameter("content", "테스트용 수정된 본문")
                .build();
        ResponseEntity<String> response = basicAuthTemplate(loginUser)
                .postForEntity(String.format("/questions/%d", originalId), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findById(originalId).isPresent()).isTrue();
        assertThat(questionRepository.findById(originalId).get().getTitle()).isEqualTo(editedTitle);
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/questions/" + originalId);
    }

    @Test
    public void update_unmatch_user() {
        Long originalId = 2L;
        User loginUser = defaultUser();
        String editedTitle = "테스트용 수정된 제목";
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("_method", "put")
                .addParameter("title", editedTitle)
                .addParameter("content", "테스트용 수정된 본문")
                .build();
        ResponseEntity<String> response = basicAuthTemplate(loginUser)
                .postForEntity(String.format("/questions/%d", originalId), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(questionRepository.findById(originalId).isPresent()).isTrue();
        assertThat(questionRepository.findById(originalId).get().getTitle()).isNotEqualTo(editedTitle);
    }

    @Test
    public void delete() {
        Long originalId = 1L;
        User loginUser = defaultUser();
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("_method", "delete")
                .build();
        ResponseEntity<String> response = basicAuthTemplate(loginUser)
                .postForEntity(String.format("/questions/%d", originalId), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(questionRepository.findById(originalId).isPresent()).isTrue();
        assertThat(questionRepository.findById(originalId).get().isDeleted()).isTrue();
        assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/questions");

    }

    @Test
    public void delete_unmatch_user() {
        Long originalId = 2L;
        User loginUser = defaultUser();
        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("_method", "delete")
                .build();
        ResponseEntity<String> response = basicAuthTemplate(loginUser)
                .postForEntity(String.format("/questions/%d", originalId), request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(questionRepository.findById(originalId).isPresent()).isTrue();
        assertThat(questionRepository.findById(originalId).get().isDeleted()).isFalse();
    }

}


