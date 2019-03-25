package codesquad.service;

import codesquad.NullEntityException;
import codesquad.UnAuthenticationException;
import codesquad.UnAuthorizedException;
import codesquad.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    public Question findById(long id) {
        return questionRepository.findById(id).orElseThrow(() -> new NullEntityException());
    }

    public Answer findByAnswerId(long id) {
        return answerRepository.findById(id).orElseThrow(() -> new NullEntityException());
    }

    @Transactional
    public Question update(User loginUser, long id, Question updatedQuestion) throws UnAuthorizedException {
        Question original = findById(id);
        if (original.isOwner(loginUser)) {
            original.update(updatedQuestion);
            return questionRepository.save(original);
        }
        throw new UnAuthorizedException();
    }

    @Transactional
    public Question delete(User loginUser, long questionId) throws UnAuthorizedException {
        Question original = findById(questionId);
        if (original.isOwner(loginUser)) {
            original.delete();
            return questionRepository.save(original);
        }
        throw new UnAuthorizedException();
    }

    public Question ownerCheck(long id, User loginUser) throws UnAuthenticationException {
        Question question = findById(id);
        if (question.isOwner(loginUser)) {
            return question;
        }
        throw new UnAuthenticationException();
    }

    public Iterable<Question> findAll() {
        return questionRepository.findByDeleted(false);
    }

    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    public Answer addAnswer(User loginUser, long questionId, String contents) {
        Answer answer = new Answer(loginUser, contents);
        answer.toQuestion(findById(questionId));
        return answerRepository.save(answer);
    }

    @Transactional
    public Answer deleteAnswer(User loginUser, long id) throws UnAuthorizedException {
        Answer answer = findByAnswerId(id);
        if (answer.isOwner(loginUser)) {
            answer.delete();
            return answerRepository.save(answer);
        }
        throw new UnAuthorizedException();
    }

    @Transactional
    public Answer updateAnswer(User loginUser, long id, String updatedContents) throws UnAuthorizedException {
        Answer answer = findByAnswerId(id);
        if (answer.isOwner(loginUser)) {
            answer.setContents(updatedContents);
            return answerRepository.save(answer);
        }
        throw new UnAuthorizedException();
    }
}
