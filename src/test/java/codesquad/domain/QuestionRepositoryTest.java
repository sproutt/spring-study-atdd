package codesquad.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static codesquad.domain.UserTest.JAVAJIGI;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void findByDeletedTest(){
        Question question = new Question("test1","test1");
        question.writeBy(JAVAJIGI);
        question.delete(JAVAJIGI);
        questionRepository.save(question);

        List<Question> questions = questionRepository.findByDeleted(false);

        assertThat(questions).doesNotContain(question);
    }

    @Test
    public void findByIdAndDeletedTest(){
        Question question = new Question("test2","test2");
        question.writeBy(JAVAJIGI);
        question.delete(JAVAJIGI);
        Question savedQuestion=questionRepository.save(question);

        Optional<Question> foundQuestion = questionRepository.findByIdAndDeleted(savedQuestion.getId(), false);

        assertThat(foundQuestion).isEmpty();
    }
}
