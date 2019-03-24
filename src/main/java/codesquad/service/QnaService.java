package codesquad.service;

import codesquad.domain.*;
import codesquad.exception.EntityDeletedException;
import codesquad.exception.QuestionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;
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

    public Iterable<Answer> findAllAnswerNotDeleted(Long questionId){
        return findByIdNotDeleted(questionId).getAnswers();
    }

    public Answer addAnswer(User loginUser, long questionId, String contents) {
        Answer answer = new Answer(loginUser, contents);
        answer.toQuestion(findByIdNotDeleted(questionId));
        return answerRepository.save(answer);
    }

    public Answer deleteAnswer(User loginUser, long id) {
        Answer answer = findAnswerByIdNotDeleted(id);
        answer.delete();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.ANSWER ,id, loginUser, answer.getCreatedAt());
        deleteHistoryService.save(deleteHistory);
        return answerRepository.save(answer);
    }

    public Question findByIdNotDeleted(Long id) {
        return questionRepository.findById(id)
                .filter(question -> !question.isDeleted())
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Question> findAllNotDeleted(){
        return questionRepository.findAll()
                .stream()
                .filter(question -> !question.isDeleted())
                .collect(Collectors.toList());
    }

    @Transactional
    public Question deleteQuestionWithAnswer(User loginUser, long id) {
        Question question = findByIdNotDeleted(id);

        List<DeleteHistory> deleteHistories = question.getAnswers().stream().map(answer -> {
            answer.delete();
            return new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser, answer.getCreatedAt());
        }).collect(Collectors.toList());

        question.delete(loginUser);
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, id, loginUser, question.getCreatedAt()));
        deleteHistoryService.saveAll(deleteHistories);
        return questionRepository.save(question);
    }

    public Answer findAnswerByIdNotDeleted(Long answerId) {
        return answerRepository.findById(answerId)
                .filter(answer -> !answer.isDeleted())
                .orElseThrow(EntityNotFoundException::new);
    }
}
