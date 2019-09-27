package codesquad.web;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

public class LoginAcceptanceTest extends AcceptanceTest {

  private static final Logger log = LoggerFactory.getLogger(LoginAcceptanceTest.class);

  private final UserRepository userRepository;

  public LoginAcceptanceTest(UserRepository userRepository){
    this.userRepository =userRepository;
  }

  @Test
  public void login() throws Exception{
    HtmlFormDataBuilder htmlFormDataBuilder = HtmlFormDataBuilder.urlEncodedForm();

    String userId = "testuser";
    String userPassword = "password";
    MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
    params.add("userId", userId);
    params.add("password", userPassword);
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, htmlFormDataBuilder.getHeaders());

    ResponseEntity<String> response = template().postForEntity("/login", request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

    User user = userRepository.findByUserId(userId).get();
    assertThat(user.getUserId()).isEqualTo(userId);
    assertThat(user.getPassword()).isEqualTo(userPassword);

    assertThat(response.getHeaders().getLocation().getPath()).startsWith("/");

  }

}
