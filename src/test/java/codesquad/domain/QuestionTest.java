package codesquad.domain;

import org.junit.Test;

public class QuestionTest {

    public static Question newQuestion(User user, Long questionId){
        Question question = new Question(questionId, "test_title", "test_contents");
        question.writeBy(user);
        return question;
    }


}
