package codesquad.web.dto;


import codesquad.domain.User;

public class LoginUserDTO {
    private String userId;
    private String password;

    public LoginUserDTO() {
    }

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

    public LoginUserDTO userInfoToDTO(String[] userInfo) {
        this.userId = userInfo[0];
        this.password = userInfo[1];
        return this;
    }

    public LoginUserDTO toDTO(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        return this;
    }
}
