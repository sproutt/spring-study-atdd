package codesquad.domain;

import codesquad.UnAuthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static codesquad.domain.UserTest.JAVAJIGI;
import static codesquad.domain.UserTest.SANJIGI;

public class QuestionTest {
    private Question question;

    @Before
    public void setUp() {
        question = new Question("title", "contents");
        question.writeBy(JAVAJIGI);
    }

    @After
    public void cleanUp() {
        question = null;
    }

    @Test
    public void delete_성공_같은_질문작성자() {
        question.delete(JAVAJIGI);
    }

    @Test
    public void delete_성공_같은_질문작성자_같은_답변작성자() {
        question.addAnswer(new Answer(JAVAJIGI, "contents"));

        question.delete(JAVAJIGI);
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_실패_다른_질문작성자() {
        question.delete(SANJIGI);
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_실패_같은_질문작성자_다른_답변작성자() {
        question.addAnswer(new Answer(SANJIGI, "contents1"));

        question.delete(JAVAJIGI);
    }
}
