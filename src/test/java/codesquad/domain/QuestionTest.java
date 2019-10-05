package codesquad.domain;

import codesquad.exception.UnAuthenticationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {

    private User originUser = UserTest.JAVAJIGI;
    private User otherUser = UserTest.SANJIGI;

    public static Question newQuestion(User user, Long questionId){
        Question question = new Question(questionId, "test_title", "test_contents");
        question.writeBy(user);
        return question;
    }

    @Test
    public void update_owner() throws Exception{
        Question question = newQuestion(originUser, 1L);
        Question updatedQuestion = newQuestion(originUser, 2L);
        question.update(originUser, updatedQuestion);

        assertThat(question.getTitle()).isEqualTo(updatedQuestion.getTitle());
        assertThat(question.getContents()).isEqualTo(updatedQuestion.getContents());
    }

    @Test(expected = UnAuthenticationException.class)
    public void update_not_owner() throws Exception{
        Question question = newQuestion(originUser, 1L);
        Question updatedQuestion = newQuestion(originUser, 2L);
        question.update(otherUser, updatedQuestion);
    }

    @Test
    public void delete_owner() throws Exception{
        User originUser = UserTest.JAVAJIGI;
        Question question = newQuestion(originUser, 1L);
        question.delete(originUser);

        assertThat(question.isDeleted()).isTrue();
    }

    @Test(expected = UnAuthenticationException.class)
    public void delete_not_owner() throws Exception{
        Question question = newQuestion(UserTest.JAVAJIGI, 1L);
        question.delete(otherUser);
    }
}
