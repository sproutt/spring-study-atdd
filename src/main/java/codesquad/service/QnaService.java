package codesquad.service;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QnaService {

    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QnaService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public Question create(User loginUser, Question question) {
        question.writeBy(loginUser);
        log.debug("question : {}", question);
        return questionRepository.save(question);
    }

    public Question findById(long id) {
        return questionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Question update(User loginUser, long questionId, Question updatedQuestion) throws Exception {
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);

        updatedQuestion = updatedQuestion.update(loginUser, question);

        return questionRepository.save(updatedQuestion);
    }

    @Transactional
    public Question deleteQuestion(User loginUser, long questionId) throws Exception {
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);

        Question deletedQuestion = question.delete(loginUser);

        return questionRepository.save(deletedQuestion);
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    public Answer addAnswer(User loginUser, long questionId, String contents) throws Exception {
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);
        Answer answer = new Answer(loginUser, contents);

        answer.toQuestion(question);

        return answerRepository.save(answer);
    }

    public Answer deleteAnswer(User loginUser, long id) throws Exception {
        Answer answer = answerRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        answer.delete(loginUser);

        return answerRepository.save(answer);
    }
}
