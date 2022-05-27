package codesquad.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;

@Service("questionService")
public class QuestionService {
	private static final Logger log = LoggerFactory.getLogger(QnaService.class);

	@Resource(name = "questionRepository")
	private QuestionRepository questionRepository;

	@Resource(name = "answerRepository")
	private AnswerRepository answerRepository;

	@Resource(name = "deleteHistoryService")
	private DeleteHistoryService deleteHistoryService;

	public Question create(User loginUser, Question question){
		question.writeBy(loginUser);
		return questionRepository.save(question);
	}


	@Transactional
	public Question updateQuestion(User loginUser, Long id, Question question) {
		Optional<Question> updatedQuestion = questionRepository.findById(id);
		updatedQuestion.get().update(loginUser, question);
		return questionRepository.save(updatedQuestion.get());
	}

	@Transactional
	public void deleteQuestion(User loginUser, Long id) throws CannotDeleteException {
		deleteHistoryService.saveAll(findQuestionById(id).delete(loginUser));
	}

	public List<Question> findAll(){
		return questionRepository.findAll();
	}

	public Question findQuestionById(Long id){
		return questionRepository.findById(id).filter(question -> !question.isDeleted()).orElseThrow(EntityNotFoundException::new);
	}

	public Question findQuestionById(User loginUser, Long id) {
		Question question = findQuestionById(id);
		if (!question.isOwner(loginUser)) {
			throw new UnAuthorizedException();
		}
		return question;
	}
}
