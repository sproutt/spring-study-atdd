package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class QnaAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);
	private HtmlFormDataBuilder formDataBuilder;
	private ResponseEntity<String> response;
	private Question question;

	@Autowired
	private QuestionRepository questionRepository;


	@Before
	public void setUp() {
		formDataBuilder = HtmlFormDataBuilder.urlEncodedForm();
		question = questionRepository.findAll().get(0);
	}

	@Test
	public void create() {
		User loginUser = defaultUser();
		addSampleQuestionData();
		response = basicAuthTemplate(loginUser)
				.postForEntity("/questions", formDataBuilder.build(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(response.getHeaders().getLocation().getPath()).startsWith("/questions");
	}

	@Test
	public void create_no_login() {
		addSampleQuestionData();
		response = template().postForEntity("/questions", formDataBuilder.build(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}


	@Test
	public void delete() {
		formDataBuilder.delete();
		StringBuilder postURL = new StringBuilder();
		postURL.append("/questions/").append(defaultQuestion().getId());
		response = basicAuthTemplate().postForEntity(postURL.toString(), formDataBuilder.build(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(response.getHeaders().getLocation().getPath()).startsWith("/questions");
	}

	@Test
	public void delete_no_authenticated() {
		formDataBuilder.delete();
		StringBuilder postURL = new StringBuilder();
		postURL.append("/quesions/").append(question.getId());
		response = template().postForEntity(postURL.toString(), formDataBuilder.build(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	private ResponseEntity<String> update(TestRestTemplate template) throws Exception {
		formDataBuilder.addParameter("title", "test2");
		formDataBuilder.addParameter("contents", "contents2");
		return template.exchange(defaultQuestion().generateUrl(), HttpMethod.PUT, formDataBuilder.build(), String.class);
	}

	@Test
	public void update() throws Exception {
		response = update(basicAuthTemplate());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(response.getHeaders().getLocation().getPath()).isEqualTo(defaultQuestion().generateUrl());
	}

	@Test
	public void update_no_authenticated() {
		response = template().exchange(defaultQuestion().generateUrl(), HttpMethod.DELETE, formDataBuilder.build(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	public void list() {
		response = template().getForEntity("/", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.debug("body : {}", response.getBody());
		assertThat(response.getBody()).contains(defaultQuestion().getTitle());
		assertThat(response.getBody()).contains(defaultQuestion().getContents());
	}

	private void addSampleQuestionData() {
		formDataBuilder
				.addParameter("title", "title")
				.addParameter("contents", "contents");
	}

}
