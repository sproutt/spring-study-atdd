package codesquad.domain;

public class QuestionTest {

    public static Question newQuestion(Long id, User loginUser) {
        Question question = new Question("오늘의 미션은?", "자동차 세차하기");
        question.writeBy(loginUser);
        return question;
    }

    public static Question updatedQuestion(String title, String contents, User loginUser) {
        Question question = new Question(title, contents);
        question.writeBy(loginUser);
        return question;
    }
}
