package codesquad.domain;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.dto.AnswerDTO;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AnswerTest {
    private User loginUser;
    private Answer answer;
    private User anotherUser;

    @Before
    public void setUp() throws Exception {
        loginUser = new User(1l, "bell", "1111", "name", "bell@gmail.com");
        anotherUser = new User();
        answer = new Answer(loginUser, "contents");
    }


    @Test
    public void update_질문한_사람과_로그인한_사람이_같은_경우() {
        answer.update(loginUser, new AnswerDTO("update"));
        assertThat(answer.getContents().equals("update"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_질문한_사람과_로그인한_사람이_다른_경우() {
        answer.update(anotherUser, new AnswerDTO("update"));
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_질문한_사람과_로그인한_사람이_다른_경우() throws CannotDeleteException {
        answer.delete(anotherUser);
    }

    @Test
    public void delete_질문한_사람과_로그인한_사람이_같은_경우() throws CannotDeleteException {
        answer.delete(loginUser);
        assertThat(answer.isDeleted()).isTrue();
    }
}
