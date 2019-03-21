package codesquad.service;

import codesquad.domain.*;
import codesquad.dto.QuestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.NoSuchElementException;
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

    public Question create(User loginUser, QuestionDTO questionDTO) {
        Question question = new Question(loginUser, questionDTO);
        log.debug("question : {}", question);
        return questionRepository.save(question);
    }

    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public Question update(User loginUser, long id, QuestionDTO updatedQuestionDTO) {
        Question question = questionRepository
                .findById(id).orElseThrow(NoSuchElementException::new);
        question.update(loginUser, updatedQuestionDTO);

        return questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(User loginUser, long questionId) throws Exception {
        Question question = questionRepository
                .findById(questionId).orElseThrow(NoSuchElementException::new);
        question.delete(loginUser);
    }

    public boolean isQuestionWriter(User loginUser, long id) {
        return questionRepository.findById(id).orElseThrow(NoSuchElementException::new).isOwner(loginUser);
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
