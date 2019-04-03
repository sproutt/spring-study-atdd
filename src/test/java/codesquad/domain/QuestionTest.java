package codesquad.domain;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.dto.QuestionDTO;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    private User loginUser;
    private Question question;
    private User anotherUser;

    @Before
    public void setUp() throws Exception {
        loginUser = new User(1l, "bell", "1111", "name", "bell@gmail.com");
        anotherUser = new User();
        question = new Question("title", "contents");
        question.writeBy(loginUser);
    }


    @Test
    public void update_질문한_사람과_로그인한_사람이_같은_경우() {
        question.update(loginUser, new QuestionDTO("title", "update"));
        assertThat(question.getContents().equals("update"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_질문한_사람과_로그인한_사람이_다른_경우() {
        question.update(anotherUser, new QuestionDTO("title", "update"));
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_질문한_사람과_로그인한_사람이_다른_경우() throws CannotDeleteException {
        question.delete(anotherUser);
    }

    @Test
    public void delete_질문한_사람과_로그인한_사람이_같으면서_답변이_없는_경우() throws CannotDeleteException {
        question.delete(loginUser);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    public void delete_질문한_사람과_로그인한_사람이_같으면서_답변의_글쓴이도_같은_경우() throws CannotDeleteException {
        Answer answer = new Answer(loginUser, "hihi");
        question.addAnswer(answer);
        question.delete(loginUser);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_질문한_사람과_로그인한_사람이_같으면서_답변의_글쓴이가_다른_경우() throws CannotDeleteException {
        Answer answer = new Answer(anotherUser, "hihi");
        question.addAnswer(answer);
        question.delete(loginUser);
    }
}
