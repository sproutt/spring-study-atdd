package codesquad.service;

import codesquad.exception.CannotDeleteException;
import codesquad.domain.*;
import codesquad.exception.UnAuthenticationException;
import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        question.update(loginUser, updatedQuestion);
        questionRepository.save(question);

        return question;
    }

    @Transactional
    public Question deleteQuestion(User loginUser, long questionId) throws Exception {
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);

        question.delete(loginUser);
        questionRepository.save(question);

        return question;
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

        question.addAnswer(answer);
        questionRepository.save(question);
        return answer;
    }

    public Answer deleteAnswer(User loginUser, long id) throws Exception {
        Answer answer = answerRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        answer.delete(loginUser);
        answerRepository.save(answer);

        return answer;
    }
}
