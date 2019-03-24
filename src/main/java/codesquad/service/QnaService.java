package codesquad.service;

import codesquad.exception.CannotDeleteException;
import codesquad.domain.*;
import codesquad.exception.QuestionDeletedException;
import codesquad.exception.QuestionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    @Transactional
    public Question update(User loginUser,  QuestionDTO updatedQuestionDTO, long id) {
        return questionRepository.save(findById(id).update(loginUser, updatedQuestionDTO));
    }

    @Transactional
    public void delete(User loginUser, long questionId){
        questionRepository.save(findById(questionId).delete(loginUser));
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

    public Question findByIdNotDeleted(Long id) {
        return questionRepository.findById(id)
                .filter(question -> !question.isDeleted())
                .orElseThrow(QuestionDeletedException::new);

    }

    public List<Question> findAllNotDeleted(){
        return questionRepository.findAll().stream().filter(question -> !question.isDeleted()).collect(Collectors.toList());
    }

    public Question deleteQuestionWithAnswer(User loginUser, long id) {
        return null;
    }
}
