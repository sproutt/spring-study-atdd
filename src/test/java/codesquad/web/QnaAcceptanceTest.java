package codesquad.web;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static org.assertj.core.api.Assertions.*;

public class QnaAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

	private ResponseEntity<String> create(TestRestTemplate template) throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodeForm();

		builder.addParameter("title", "test title");
		builder.addParameter("contents", "test content");
		HttpEntity<MultiValueMap<String, Object>> request = builder.build();

		return template.postForEntity("/questions", request, String.class);
	}

	@Test
	public void create() throws Exception {
		ResponseEntity<String> response = create(basicAuthTemplate());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		log.debug("questions create : {}", response.getBody());
	}

	@Test
	public void create_no_login() throws Exception {
		ResponseEntity<String> response = create(template());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

}
