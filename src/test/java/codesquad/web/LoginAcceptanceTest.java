package codesquad.web;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import codesquad.domain.UserRepository;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.*;

public class LoginAcceptanceTest extends AcceptanceTest{

	@Autowired
	private UserRepository userRepository;

	@Test
	public void login() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String userId = "javajigi";
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("userId", userId);
		params.add("password", "test");

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
		assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
		assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users");
	}

	@Test
	public void login_failed_mismatch_password(){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String userId = "javajigi";
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("userId", userId);
		params.add("password", "1234");

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(userRepository.findByUserId(userId).isPresent()).isTrue();
	}

	@Test
	public void login_failed_mismatch_id(){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String userId = "javajig";
		String password = "12345";
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("userId", userId);
		params.add("password", password);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(userRepository.findByUserId(userId).isPresent()).isFalse();
		assertThat(HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()));
	}

}
