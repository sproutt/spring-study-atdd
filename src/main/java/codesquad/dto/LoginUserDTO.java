package codesquad.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserDTO {
    private String userId;
    private String password;
}
