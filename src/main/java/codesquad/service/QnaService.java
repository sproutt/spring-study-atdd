package codesquad.service;

import codesquad.UnAuthorizedException;
import codesquad.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("question not found"));

        if (!question.isOwner(loginUser)) {
            throw new UnAuthorizedException("mismatch writer");
        }

        question.update(updateQuestion);
        return questionRepository.save(question);
    }

    @Transactional
    public Question deleteQuestion(User loginUser, Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("question not found"));

        deleteHistoryService.saveAll(question.delete(loginUser));

        return questionRepository.save(question);
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    public Answer addAnswer(User loginUser, Long questionId, String contents) {
        Answer answer = new Answer(loginUser, contents);
        answer.toQuestion(questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("question not found")));

        return answerRepository.save(answer);
    }

    public Answer deleteAnswer(User loginUser, Long id) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("answer not found"));

        answer.delete(loginUser);
        return answerRepository.save(answer);
    }
}
