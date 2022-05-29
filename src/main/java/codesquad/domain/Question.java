package codesquad.domain;

import codesquad.CannotDeleteException;
import org.hibernate.annotations.Where;
import support.domain.AbstractEntity;
import support.domain.UrlGeneratable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Question extends AbstractEntity implements UrlGeneratable {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @Where(clause = "deleted = false")
    @OrderBy("id ASC")
    private final List<Answer> answers = new ArrayList<>();
    @Size(min = 3, max = 100)
    @Column(length = 100, nullable = false)
    private String title;
    @Size(min = 3)
    @Lob
    private String contents;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    private boolean deleted = false;

    public Question() {
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Question(Long id, String title, String contents) {
        super(id);
        this.title = title;
        this.contents = contents;
    }

    public String getTitle() {
        return title;
    }

    public Question setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public Question setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public User getWriter() {
        return writer;
    }

    public void writeBy(User loginUser) {
        this.writer = loginUser;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        answers.add(answer);
    }

    public boolean isOwner(User loginUser) {
        return writer.equals(loginUser);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void update(Question updatedQuestion) {
        this.title = updatedQuestion.getTitle();
        this.contents = updatedQuestion.getContents();
        this.writer = updatedQuestion.writer;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {

        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("삭제 권한이 없습니다.");
        }

        if(!hasSameWriterWithAnswers()) {
            throw new CannotDeleteException("삭제 권한이 없습니다.");
        }

        List<DeleteHistory> deleteHistories = new ArrayList<>();

        answers.stream()
               .filter(answer -> answer.isOwner(writer))
               .forEach(answer -> deleteHistories.add(answer.delete(loginUser)));

        this.deleted = true;

        return deleteHistories;
    }

    @Override
    public String generateUrl() {
        return String.format("/questions/%d", getId());
    }

    @Override
    public String toString() {
        return "Question [id=" + getId() + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
    }

    public boolean equalsTitleAndContents(Question target) {
        if(Objects.isNull(target)) {
            return false;
        }

        return this.title.equals(target.getTitle()) &&
                this.contents.equals(target.getContents());
    }

    public boolean hasSameWriterWithAnswers() {
        return answers.stream()
                .allMatch(answer -> answer.isOwner(writer));
    }
}
