package codesquad.domain;

import codesquad.UnAuthorizedException;
import org.assertj.core.api.Java6AbstractBDDSoftAssertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Test
    public void update_question() throws Exception {
        Question origin = new Question("테스트용 제목", "테스트용 내용");
        origin.writeBy(JAVAJIGI);
        Question target = new Question("변경된 제목", "변경된 내");

        origin.update(JAVAJIGI, target);

        assertThat(origin.getTitle()).isEqualTo(target.getTitle());
        assertThat(origin.getContents()).isEqualTo(target.getContents());
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_not_owner() throws Exception {
        Question origin = new Question("테스트용 제목", "테스트용 내용");
        origin.writeBy(JAVAJIGI);
        Question target = new Question("변경된 제목", "변경된 내");

        origin.update(SANJIGI, target);
    }
}
