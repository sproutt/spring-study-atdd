package codesquad.web.dto;

import codesquad.domain.User;

public class UserLoginDTO {

    private String userId;
    private String password;

    public UserLoginDTO(String[] userInfo) {
        this.userId = userInfo[0];
        this.password = userInfo[1];
    }

    public UserLoginDTO(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
    }

    public UserLoginDTO() { }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
