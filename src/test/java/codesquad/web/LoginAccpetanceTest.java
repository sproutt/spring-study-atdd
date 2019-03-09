package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.util.Arrays;

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
	public void loginForm() throws Exception{
		ResponseEntity<String> response = template.getForEntity("/users/login", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void login_sucess() throws Exception{
		HttpHeaders header = new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		String userId = "javajigi";
		String password = "test";
		params.add("userId",userId);
		params.add("password", password);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params , header);
		ResponseEntity<String> response = template.postForEntity("/users/login", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
		assertThat(response.getHeaders().getLocation().getPath()).isEqualTo("/users");
	}

	@Test
	public void login_fail() throws Exception{
		HttpHeaders header = new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		String userId = "javajigi1";
		String password = "test";
		params.add("userId",userId);
		params.add("password", password);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params , header);
		ResponseEntity<String> response = template.postForEntity("/users/login", request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(userRepository.findByUserId(userId).isPresent()).isFalse();
	}
}
