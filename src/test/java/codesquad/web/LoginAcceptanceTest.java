package codesquad.web;

import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginAcceptanceTest extends AcceptanceTest {

	@Test
	public void login_success() throws Exception {
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
		assertThat(response.getHeaders().getLocation().getPath()).startsWith("/users");
	}

	@Test
	public void login_fail() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String userId = "test";
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("userId", userId);
		params.add("password", "test");
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

		ResponseEntity<String> response = template().postForEntity("/users/login", request, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
