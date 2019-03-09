package codesquad.web;

import codesquad.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import support.utils.HtmlFormDataBuilder;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginAccpetanceTest {
	private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void loginForm() throws Exception {
		ResponseEntity<String> response = template.getForEntity("/users/login", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void login_sucess() throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		String userId = "javajigi";
		String password = "test";
		builder.addParameter("userId", userId);
		builder.addParameter("password", password);

		ResponseEntity<String> response = template.postForEntity("/users/login", builder.build(), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
		assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/users");
	}

	@Test
	public void login_fail() throws Exception {
		HtmlFormDataBuilder builder = HtmlFormDataBuilder.urlEncodedForm();
		String userId = "javajigi";
		String password = "test";
		builder.addParameter("userId", userId);
		builder.addParameter("password", password);

		ResponseEntity<String> response = template.postForEntity("/users/login", builder.build(), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(userRepository.findByUserId(userId).isPresent()).isFalse();
	}
}
