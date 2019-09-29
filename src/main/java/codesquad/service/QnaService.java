package codesquad.service;

import codesquad.exception.CannotDeleteException;
import codesquad.domain.*;
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

    public QnaService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
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
    public Question update(User loginUser, long id, Question updatedQuestion) {
        // TODO 수정 기능 구현
        return null;
    }

    @Transactional
    public void deleteQuestion(User loginUser, long questionId) throws CannotDeleteException {
        // TODO 삭제 기능 구현
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    public Answer addAnswer(User loginUser, long questionId, String contents) {
        // TODO 답변 추가 기능 구현
        return null;
    }

    public Answer deleteAnswer(User loginUser, long id) {
        // TODO 답변 삭제 기능 구현 
        return null;
    }
}
