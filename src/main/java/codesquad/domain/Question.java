package codesquad.domain;

import codesquad.exception.UnAuthenticationException;
import javax.naming.AuthenticationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;
import support.domain.AbstractEntity;
import support.domain.UrlGeneratable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Question extends AbstractEntity implements UrlGeneratable {

    @Size(min = 3, max = 100)
    @Column(length = 100, nullable = false)
    private String title;

    @Size(min = 3)
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private List<Answer> answers = new ArrayList<>();

    private boolean deleted = false;

    public Question(String title, String contents) {
        this(0L, title, contents);
    }

    public Question(Long id, String title, String contents) {
        super(id);
        this.title = title;
        this.contents = contents;
    }

    public void writeBy(User loginUser) {
        this.writer = loginUser;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    private void isOwner(User loginUser) throws Exception {
        if (!writer.equals(loginUser)) {
            throw new UnAuthenticationException();
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Question update(User loginUser, Question updatedQuestion) throws Exception {
        this.isOwner(loginUser);

        this.title = updatedQuestion.title;
        this.contents = updatedQuestion.contents;

        return this;
    }

    public void delete(User loginUser) throws Exception {
        this.isOwner(loginUser);
        deleted = true;
    }

    @Override
    public String generateUrl() {
        return String.format("/questions/%d", getId());
    }

}
