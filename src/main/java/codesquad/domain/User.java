package codesquad.domain;

import codesquad.exception.UnAuthorizedException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import support.domain.AbstractEntity;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class User extends AbstractEntity {

    public static final GuestUser GUEST_USER = new GuestUser();

    @Size(min = 3, max = 20)
    @Column(unique = true, nullable = false)
    private String userId;

    @Size(min = 3, max = 20)
    @Column(nullable = false)
    private String password;

    @Size(min = 3, max = 20)
    @Column(nullable = false)
    private String name;

    @Size(max = 50)
    private String email;

    public User(String userId, String password, String name, String email) {
        this(0L, userId, password, name, email);
    }

    public User(long id, String userId, String password, String name, String email) {
        super(id);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User update(User loginUser, User target) {
        if (Objects.isNull(target)) {
            throw new EntityNotFoundException();
        }

        if (!matchUserId(loginUser.userId) || !matchPassword(target.password)) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;

        return this;
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    public boolean matchPassword(String targetPassword) {
        return password.equals(targetPassword);
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return matchNameAndEmail(target.name, target.email);
    }

    public boolean equals(User target) {
        if (!super.equals(target) || !matchInfo(target)) {
            return false;
        }

        return true;
    }

    private boolean matchInfo(User target) {
        return matchUserId(target.userId) && matchPassword(target.password) && matchNameAndEmail(target.name,
            target.email);
    }

    private boolean matchNameAndEmail(String name, String email) {
        return this.name.equals(name) && this.email.equals(email);
    }

    @JsonIgnore
    public boolean isGuestUser() {
        return false;
    }

    private static class GuestUser extends User {

        @Override
        public boolean isGuestUser() {
            return true;
        }
    }

}
