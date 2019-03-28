package codesquad.domain;

import codesquad.AlreadyDeletedException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;


public class QuestionTest {

    User user = new User();
    Question question = new Question();

    @Test
    public void update() {
        String title = "title";
        String contents = "contents";
        Question updatedQuestion = new Question(title, contents);
        question.writeBy(user);
        question.update(user, updatedQuestion);
        assertThat(question.getTitle().equals(title));
        assertThat(question.getContents().equals(contents));
    }

    @Test
    public void delete() throws Exception {
        question.writeBy(user);
        question.delete(user);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test(expected = AlreadyDeletedException.class)
    public void delete_fail_question_not_exist() throws Exception {
        question.writeBy(user);
        question.delete(user);
        question.delete(user);
    }

    @Test
    public void delete_success_same_user_with_no_answers() throws Exception {
        question.writeBy(user);
        assertThat(question.hasNoAnswers(user)).isTrue();

        question.delete(user);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    public void delete_success_same_user_with_same_answers_writer() {

    }

    @Test
    public void delete_fail_same_user_but_different_answers_writer() {

    }
}
