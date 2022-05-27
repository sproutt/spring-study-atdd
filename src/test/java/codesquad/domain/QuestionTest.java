package codesquad.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {
    public static final Question TEST_QUESTION = new Question("오늘의 미션은?", "자동차 세차하기");

    public static Question newQuestion(Long id, User loginUser) {
        Question question = new Question(id, "오늘의 미션은?", "자동차 세차하기");
        question.writeBy(loginUser);
        return question;
    }

    public static Question newQuestion(User loginUser) {
        Question question = new Question(0L, "오늘의 미션은", "자동차 세차하기");
        question.writeBy(loginUser);
        return question;
    }

    public static Question updatedQuestion(Long id, String title, String contents, User loginUser) {
        Question question = new Question(id, title, contents);
        question.writeBy(loginUser);
        return question;
    }

    @Test
    public void update() {
        User origin = UserTest.JAVAJIGI;
        User loginUser = origin;
        Question originQuestion = newQuestion(1L, origin);
        Question targetQuestion = updatedQuestion(1L, "오늘의 할 일은?", "자동차 주차하기", loginUser);

        originQuestion.update(targetQuestion);

        assertThat(originQuestion.getTitle()).isEqualTo(targetQuestion.getTitle());
        assertThat(originQuestion.getContents()).isEqualTo(targetQuestion.getContents());
    }

    @Test
    public void delete() {
        User loginUser = UserTest.JAVAJIGI;
        Question question = newQuestion(1L, loginUser);

        question.delete();

        assertThat(question.isDeleted()).isTrue();
    }
}
