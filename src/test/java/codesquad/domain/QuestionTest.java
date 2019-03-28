package codesquad.domain;

import codesquad.exception.UnAuthorizedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "자바지기", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    public static Question originQuestion;

    @Before
    public void setup() {
        originQuestion = new Question("테스트용 제목", "테스트용 내용");
        originQuestion.writeBy(JAVAJIGI);
        Answer answer1 = new Answer(JAVAJIGI,"contents for answer1");
        Answer answer2 = new Answer(JAVAJIGI,"contents for answer2");
        originQuestion.addAnswer(answer1);
        originQuestion.addAnswer(answer2);
    }

    @Test
    public void update_question() throws Exception {
        Question target = new Question("변경된 제목", "변경된 내");

        originQuestion.update(JAVAJIGI, new QuestionDTO(target));

        assertThat(originQuestion.getTitle()).isEqualTo(target.getTitle());
        assertThat(originQuestion.getContents()).isEqualTo(target.getContents());
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_not_owner() throws Exception {
        Question target = new Question("변경된 제목", "변경된 내");

        originQuestion.update(SANJIGI, new QuestionDTO(target));
    }

    @Test
    public void delete_question() throws Exception {
        originQuestion.delete(JAVAJIGI);

        assertThat(originQuestion.isDeleted()).isEqualTo(true);
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_not_owner() throws Exception {
        originQuestion.delete(SANJIGI);
    }

    @Test(expected =UnAuthorizedException.class)
    public void delete_question_with_answer_by_other() throws Exception{
        Answer answer = new Answer(SANJIGI, "title for other");
        originQuestion.addAnswer(answer);

        originQuestion.delete(JAVAJIGI);
    }
}
