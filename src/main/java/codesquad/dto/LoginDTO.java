package codesquad.dto;

import codesquad.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {
	private String userId;
	private String password;

	public LoginDTO(User user) {
		this.userId = user.getUserId();
		this.password = user.getPassword();
	}
}
