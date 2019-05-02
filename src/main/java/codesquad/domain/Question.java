package codesquad.domain;

import codesquad.UnAuthorizedException;
import org.hibernate.annotations.Where;
import support.domain.AbstractEntity;
import support.domain.UrlGeneratable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static codesquad.domain.ContentType.ANSWER;
import static codesquad.domain.ContentType.QUESTION;

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

    private LocalDateTime createDate = LocalDateTime.now();

    public Question() {
    }

    public Question(String title, String contents) {
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

    @Override
    public String generateUrl() {
        return String.format("/questions/%d", getId());
    }

    @Override
    public String toString() {
        return "Question [id=" + getId() + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
    }

    public Question update(User loginUser, Question newQuestion) {
        if (!isOwner(loginUser)) {
            throw new UnAuthorizedException();
        }
        this.title = newQuestion.getTitle();
        this.contents = newQuestion.getContents();
        return this;
    }

    public List<DeleteHistory> delete(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        if(isOwner(loginUser)&&isAnswerWrittenByWriter(loginUser)) {
            this.deleted = true;
            deleteHistories.add(new DeleteHistory(QUESTION,getId(),getWriter(),createDate));
            deleteHistories.addAll(deleteAllAnswer());
            return deleteHistories;
        }
        throw new UnAuthorizedException();
    }

    private List<DeleteHistory> deleteAllAnswer() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for(Answer answer : answers){
            answer.delete();
            deleteHistories.add(new DeleteHistory(ANSWER,answer.getId(),answer.getWriter(),answer.getCreateDate()));
        }
        return deleteHistories;
    }

    public boolean isAnswerWrittenByWriter(User loginUser) {
        return answers.stream().allMatch(answer -> answer.isOwner(loginUser));
    }
}
