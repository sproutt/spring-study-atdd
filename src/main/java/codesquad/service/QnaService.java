package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.NoSuchElementException;

@Service("qnaService")
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    private final QuestionRepository questionRepository;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "deleteHistoryService")
    private DeleteHistoryService deleteHistoryService;

    public QnaService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question create(User loginUser, Question question) {
        question.writeBy(loginUser);
        log.debug("QnaService question ={}", question.getWriter());
        return questionRepository.save(question);
    }

    public Question findById(long id) {
        return questionRepository.findById(id)
                                 .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Question update(User loginUser, long id, Question newQuestion) {
        // TODO 수정 기능 구현
        Question question = questionRepository.findById(id)
                                              .filter(s -> s.getWriter()
                                                            .equals(loginUser))
                                              .orElseThrow(NoSuchElementException::new);

        log.debug("QnaService update() question.getWriter() ={}", question.getWriter());
        return question.update(newQuestion);
    }

    @Transactional
    public void deleteQuestion(User loginUser, long questionId) throws CannotDeleteException {
        // TODO 삭제 기능 구현
        Question question = questionRepository.findById(questionId)
                                              .filter(s -> s.getWriter()
                                                            .equals(loginUser))
                                              .orElseThrow(NoSuchElementException::new);
        log.debug("QnaService deleteQuestion setDeleted() called");
        question.setDeleted(true);
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
