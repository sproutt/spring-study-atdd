package codesquad.web;

		import codesquad.domain.UserRepository;
		import org.junit.Test;
		import org.springframework.beans.factory.annotation.Autowired;
		import support.HtmlFormDataBuilder;
		import support.test.AcceptanceTest;

public class LoginAcceptanceTest extends AcceptanceTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void login() throws Exception {
		HtmlFormDataBuilder.urlEncodedForm()
				.addParameter("userid", "jehyeon")
				.addParameter("passwrd", "1234")
				.build();

	}
}
