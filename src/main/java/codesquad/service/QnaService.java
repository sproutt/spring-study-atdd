package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service("qnaService")
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "deleteHistoryService")
    private DeleteHistoryService deleteHistoryService;

    public Question create(User loginUser, Question question) {
        question.writeBy(loginUser);
        log.debug("question : {}", question);
        return questionRepository.save(question);
    }

    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public Question update(User loginUser, Long id, Question updateQuestion) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("question not found"));

        if (!question.isOwner(loginUser)) {
            throw new UnAuthorizedException("mismatch writer");
        }

        question.update(updateQuestion);
        return questionRepository.save(question);
    }

    @Transactional
    public Question deleteQuestion(User loginUser, Long id) throws CannotDeleteException {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("question not found"));

        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException("mismatch writer");
        }

        question.delete();
        return questionRepository.save(question);
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    public Answer addAnswer(User loginUser, Long questionId, String contents) {
        // TODO 답변 추가 기능 구현
        return null;
    }

    public Answer deleteAnswer(User loginUser, Long id) {
        // TODO 답변 삭제 기능 구현 
        return null;
    }
}
