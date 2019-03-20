package codesquad.service;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service("questionService")
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question add(Question question) {
        questionRepository.save(question);
        return question;
    }

    public Question findById(long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
                                 .orElseThrow(EntityNotFoundException::new);
    }

    public List<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public Question update(long id, Question target) {
        Question question = findById(id);
        question.update(target);
        questionRepository.save(question);
        return question;
    }

    public void delete(long id) {
        Question question = findById(id);
        question.setDeleted(true);
    }
}
