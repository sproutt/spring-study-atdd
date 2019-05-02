package codesquad.service;

import codesquad.UnAuthenticationException;
import codesquad.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;

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

    public Question findQuestionById(long id) throws Exception {
        return questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    public Answer findAnswerById(long id) {
        return answerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional
    public Question update(User loginUser, long id, Question updatedQuestion) throws Exception {
        Question original = findQuestionById(id);
        original.update(loginUser, updatedQuestion);
        return questionRepository.save(original);
    }

    @Transactional
    public Question delete(User loginUser, long questionId) throws Exception {
        Question original = findQuestionById(questionId);
        deleteHistoryService.saveAll(original.delete(loginUser));
        return questionRepository.save(original);

    }

    public Question ownerCheck(long id, User loginUser) throws Exception {
        Question question = findQuestionById(id);
        if (!question.isOwner(loginUser)) {
            throw new UnAuthenticationException();
        }
        return question;
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    public Answer addAnswer(User loginUser, long questionId, String contents) throws Exception {
        Answer answer = new Answer(loginUser, contents);
        answer.toQuestion(findQuestionById(questionId));
        return answerRepository.save(answer);
    }

    @Transactional
    public Answer deleteAnswer(User loginUser, long id) throws Exception {
        Answer answer = findAnswerById(id);
        return answerRepository.save(answer.delete(loginUser));
    }

    @Transactional
    public Answer updateAnswer(User loginUser, long id, String updatedContents) throws Exception {
        Answer answer = findAnswerById(id);
        return answerRepository.save(answer.updateContents(loginUser, updatedContents));
    }
}
