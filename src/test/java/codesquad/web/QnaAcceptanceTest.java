package codesquad.web;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static org.assertj.core.api.Assertions.*;

public class QnaAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

	@Test
	@Order(1)
	public void create() throws Exception {
		ResponseEntity<String> response = create(basicAuthTemplate());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		log.debug("questions create : {}", response.getBody());
	}

	@Test
	public void create_no_login() throws Exception {
		ResponseEntity<String> response = create(template());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	@Order(2)
	public void show_questions() {
		ResponseEntity<String> response = template().getForEntity("/", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.debug("questions list body : {}", response.getBody());
		assertThat(response.getBody()).isNotEmpty();
	}

	@Test
	@Order(2)
	public void show_exist_question() {
		ResponseEntity<String> response = template().getForEntity("/questions/1", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.debug("body : {}", response.getBody());
	}

	@Test
	@Order(2)
	public void show_non_exist_question() {
		ResponseEntity<String> response = template().getForEntity("/question/100", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<String> create(TestRestTemplate template) {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodeForm();

		builder.addParameter("title", "test title");
		builder.addParameter("contents", "test content");
		HttpEntity<MultiValueMap<String, Object>> request = builder.build();

		return template.postForEntity("/questions", request, String.class);
	}

	@Test
	@Order(3)
	public void question_update_with_authorized_writer() {
		ResponseEntity<String> response = update(basicAuthTemplate());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
	}

	@Test
	@Order(3)
	public void question_update_with_no_login() {
		ResponseEntity<String> response = update(template());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	@Order(3)
	public void question_update_with_unauthorized_writer() {
		ResponseEntity<String> response = update(basicAuthTemplate(findByUserId("sanjigi")));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	private ResponseEntity<String> update(TestRestTemplate template) {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodeForm();
		builder.addParameter("_method", "put");
		builder.addParameter("title", "update title");
		builder.addParameter("content", "update content");

		HttpEntity<MultiValueMap<String, Object>> request = builder.build();
		return template.postForEntity("/questions/1", request, String.class);
	}

}
