package codesquad.service;

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
import java.util.stream.Collectors;

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
        Question question = new Question(questionDTO.getTitle(), questionDTO.getContent());
        question.writeBy(loginUser);
        return questionRepository.save(question);
    }

    @Transactional
    public Question update(User loginUser, QuestionDTO updatedQuestionDTO, long id) {
        return questionRepository.save(findById(id).update(loginUser, updatedQuestionDTO));
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    public Iterable<Answer> findAllAnswer(Long questionId) {
        return findById(questionId).getAnswers();
    }

    public Answer addAnswer(User loginUser, long questionId, String contents) {
        Answer answer = new Answer(loginUser, contents);
        answer.toQuestion(findById(questionId));
        return answerRepository.save(answer);
    }

    public Answer deleteAnswer(User loginUser, long id) {
        Answer answer = answerRepository.findByIdAndDeleted(id, false).orElseThrow(EntityNotFoundException::new);
        answer.delete();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER, id, loginUser, answer.getCreatedAt());
        deleteHistoryService.save(deleteHistory);
        return answerRepository.save(answer);
    }

    public Question findById(Long id) {
        return questionRepository.findByIdAndDeleted(id, false).orElseThrow(EntityNotFoundException::new);
    }

    public List<Question> findAllNotDeleted() {
        return questionRepository.findAll().stream().filter(question -> !question.isDeleted()).collect(Collectors.toList());
    }

    @Transactional
    public List<DeleteHistory> delete(User loginUser, Long questionId) {
        Question question = findById(questionId);
        List<DeleteHistory> deleteHistories = question.delete(loginUser);
        questionRepository.save(question);
        return deleteHistoryService.saveAll(deleteHistories);

    }

    public Answer findAnswerByIdNotDeleted(Long answerId) {
        return answerRepository.findById(answerId).filter(answer -> !answer.isDeleted()).orElseThrow(EntityNotFoundException::new);
    }
}
