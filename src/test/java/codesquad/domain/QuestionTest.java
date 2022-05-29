package codesquad.domain;

import codesquad.CannotDeleteException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuestionTest {
    private Question question;
    private User user;

    @Before
    public void setUp() {
        question = new Question("title1", "contents1");
        user = UserTest.JAVAJIGI;

        question.writeBy(user);
    }

    @Test
    public void 질문한_사람과_로그인한_사람이_다른_경우_삭제가_안되는지_테스트() throws CannotDeleteException {
        //given
        User otherUser = UserTest.SANJIGI;

        //when
        Question result = question.delete(otherUser);

        //then
        assertFalse(result.isDeleted());
    }

    @Test
    public void 질문한_사람과_로그인한_사람이_같으면서_답변이_없는_경우_삭제가_잘_되는지_테스트() {
        //given
        //when
        Question result = question.delete(user);

        //then
        assertTrue(result.isDeleted());
    }

    @Test
    public void 질문한_사람과_로그인한_사람이_같으면서_답변의_글쓴이도_같은_경우_삭제가_잘_되는지_테스트() {
        //given
        Answer answer = new Answer(user, "contents1");
        question.addAnswer(answer);

        //when
        Question result = question.delete(user);

        //then
        assertTrue(result.isDeleted());
    }

    @Test
    public void 질문한_사람과_로그인한_사람이_같으면서_답변의_글쓴이가_다른_경우_삭제가_안되는지_테스트() {
        //given
        User otherUser = UserTest.SANJIGI;
        Answer answer = new Answer(otherUser, "contents1");

        question.addAnswer(answer);

        //when
        Question result = question.delete(user);

        //then
        assertFalse(result.isDeleted());
    }

}