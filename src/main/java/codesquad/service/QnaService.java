package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthenticationException;
import codesquad.domain.*;
import codesquad.web.dto.QuestionDto;
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

    public Question create(User loginUser, Question question) {
        question.writeBy(loginUser);
        log.debug("question : {}", question);
        return questionRepository.save(question);
    }

    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public Question update(User loginUser, long id, QuestionDto updatedQuestionDto) throws UnAuthenticationException {
        if (loginUser == null) {
            throw new UnAuthenticationException("질문 작성자만 수정이 가능합니다");
        }

        Question savedQuestion = questionRepository.findById(id)
                                                   .orElseThrow(NoSuchElementException::new);

        if(!savedQuestion.isOwner(loginUser)){
            throw new UnAuthenticationException("질문 작성자만 수정이 가능합니다");
        }

        return savedQuestion.update(updatedQuestionDto);
    }

    @Transactional
    public void deleteQuestion(User loginUser, long questionId) throws CannotDeleteException {
        if(loginUser == null){
            throw new CannotDeleteException("질문 작성자만 삭제 가능합니다");
        }

        Question savedQuestion = questionRepository.findById(questionId)
                                                   .orElseThrow(NoSuchElementException::new);

        if (!savedQuestion.isOwner(loginUser)) {
            throw new CannotDeleteException("질문 작성자만 삭제 가능합니다");
        }

        log.info("savedQuestion = {}", savedQuestion);

        savedQuestion.delete();
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
