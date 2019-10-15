package codesquad.domain;

import static org.assertj.core.api.Assertions.assertThat;

import codesquad.exception.UnAuthorizedException;
import org.junit.Test;

public class QuestionTest {

    private final User originUser = UserTest.JAVAJIGI;
    private final User otherUser = UserTest.SANJIGI;

    private final Long QUESTION_ID = 1L;
    private final Long QUESTION_OTHER_ID = 2L;

    public static Question newQuestion(User user, Long questionId){
        Question question = new Question(questionId, "test_title", "test_contents");
        question.writeBy(user);
        return question;
    }

    @Test
    public void update_owner() throws Exception{
        Question question = newQuestion(originUser, QUESTION_ID);
        Question updatedQuestion = newQuestion(originUser, QUESTION_OTHER_ID);
        question.update(originUser, updatedQuestion);

        assertThat(question.getTitle()).isEqualTo(updatedQuestion.getTitle());
        assertThat(question.getContents()).isEqualTo(updatedQuestion.getContents());
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_not_owner() throws Exception{
        Question question = newQuestion(originUser, QUESTION_ID);
        Question updatedQuestion = newQuestion(originUser, QUESTION_OTHER_ID);
        question.update(otherUser, updatedQuestion);
    }

    @Test
    public void delete_owner() throws Exception{
        User originUser = UserTest.JAVAJIGI;
        Question question = newQuestion(originUser, QUESTION_ID);
        question.delete(originUser);

        assertThat(question.isDeleted()).isTrue();
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_not_owner() throws Exception{
        Question question = newQuestion(UserTest.JAVAJIGI, QUESTION_ID);
        question.delete(otherUser);
    }
}
