package codesquad.web;


import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import codesquad.domain.UserRepository;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static org.assertj.core.api.Assertions.*;

public class LoginAcceptanceTest extends AcceptanceTest{

	@Autowired
	private UserRepository userRepository;

	private HtmlFormDataBuilder builder;

	@Before
	public void init() {
		builder = HtmlFormDataBuilder.urlEncodeForm();
	}

	@Test
	public void login() {
		String userId = "javajigi";
		builder.addParameter("userId", userId);
		builder.addParameter("password", "test");

		HttpEntity<MultiValueMap<String, Object>> request = builder.build();
		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
		assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users");
	}

	@Test
	public void login_failed_mismatch_password(){
		String userId = "javajigi";
		builder.addParameter("userId", userId);
		builder.addParameter("password", "1234");
		HttpEntity<MultiValueMap<String, Object>> request = builder.build();
		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
	}

	@Test
	public void login_failed_mismatch_id(){


		String userId = "javajig";
		String password = "12345";

		builder.addParameter("userId", userId);
		builder.addParameter("password", password);

		HttpEntity<MultiValueMap<String, Object>> request = builder.build();

		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(userRepository.findByUserId(userId).isPresent()).isFalse();
		assertThat(HttpStatus.UNAUTHORIZED).isEqualTo(response.getStatusCode());
	}

}
