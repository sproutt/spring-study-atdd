package codesquad.service;

import codesquad.*;
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

    public Question findById(long id) {
        return questionRepository.findById(id).orElseThrow(() -> new NullEntityException());
    }

    public Answer findByAnswerId(long id) {
        return answerRepository.findById(id).orElseThrow(() -> new NullEntityException());
    }

    @Transactional
    public Question update(User loginUser, long id, Question updatedQuestion) throws UnAuthenticationException {
        Question original = findById(id);
        if (original.isOwner(loginUser)) {
            original.update(updatedQuestion);
            return original;
        }
        throw new UnAuthorizedException();
    }

    @Transactional
    public void delete(User loginUser, long questionId) throws UnAuthenticationException {
        Question original = findById(questionId);
        if (original.isOwner(loginUser)) {
            original.delete();
        }
        throw new UnAuthenticationException();
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
        // TODO 답변 추가 기능 구현
        Answer answer = new Answer(loginUser,contents);
        answerRepository.save(answer);
        return answer;
    }

    public Answer deleteAnswer(User loginUser, long id) {
        // TODO 답변 삭제 기능 구현 
        return null;
    }
}
