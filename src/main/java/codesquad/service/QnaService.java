package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.domain.*;
import codesquad.dto.AnswerDTO;
import codesquad.dto.QuestionDTO;
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

    public Question findById(long id) {
        return questionRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Question update(User loginUser, long id, QuestionDTO updatedQuestionDTO) {
        Question question = questionRepository
                .findById(id).orElseThrow(NoSuchElementException::new);
        question.update(loginUser, updatedQuestionDTO);

        return questionRepository.save(question);
    }

    @Transactional
    public Question deleteQuestion(User loginUser, long questionId) throws CannotDeleteException {
        Question question = questionRepository
                .findById(questionId).orElseThrow(NoSuchElementException::new);

        question.delete(loginUser);

        return question;
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

    public Answer addAnswer(User loginUser, long questionId, AnswerDTO answerDTO) {
        Question question = findById(questionId);
        Answer answer = new Answer(loginUser, question, answerDTO);

        question.addAnswer(answer);
        answerRepository.save(answer);
        questionRepository.save(question);

        return answer;
    }

    public Answer deleteAnswer(User loginUser, long id) throws CannotDeleteException {
        Answer answer = findAnswerById(id);
        answer.delete(loginUser);

        return answerRepository.save(answer);
    }

    public Answer findAnswerById(long id) {
        return answerRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Answer updateAnswer(User loginUser, long id, AnswerDTO answerDTO) {
        Answer answer = answerRepository.findById(id).orElseThrow(NoSuchElementException::new);
        answer.update(loginUser, answerDTO);

        return answerRepository.save(answer);
    }
}
