package codesquad.web;

import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;
import support.utils.HtmlFormDataBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

	@Autowired
	private UserRepository userRepository;

	@Test
	public void createForm() throws Exception {
		ResponseEntity<String> response = template().getForEntity("/users/form", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void create() throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		String userId = "testuser";
		builder.addParameter("userId", userId);
		builder.addParameter("password", "password");
		builder.addParameter("name", "자바지기");
		builder.addParameter("email", "javajigi@slipp.net");

		ResponseEntity<String> response = template().postForEntity("/users", builder.build(), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
		assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users");
	}

	@Test
	public void list() throws Exception {
		ResponseEntity<String> response = template().getForEntity("/users", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.debug("body : {}", response.getBody());
		assertThat(response.getBody()).contains(defaultUser().getEmail());
	}

	@Test
	public void updateForm_no_login() throws Exception {
		ResponseEntity<String> response = template().getForEntity(String.format("/users/%d/form", defaultUser().getId()),
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	@Test
	public void updateForm_login() throws Exception {
		User loginUser = defaultUser();
		ResponseEntity<String> response = basicAuthTemplate(loginUser)
				.getForEntity(String.format("/users/%d/form", loginUser.getId()), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains(defaultUser().getEmail());
	}

	@Test
	public void update_no_login() throws Exception {
		ResponseEntity<String> response = update(template());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	private ResponseEntity<String> update(TestRestTemplate template) throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		builder.addParameter("_method", "put");
		builder.addParameter("password", "test");
		builder.addParameter("name", "자바지기2");
		builder.addParameter("email", "javajigi@slipp.net");

		return template.postForEntity(String.format("/users/%d", defaultUser().getId()), builder.build(), String.class);
	}

	@Test
	public void update() throws Exception {
		ResponseEntity<String> response = update(basicAuthTemplate());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users");
	}
}
