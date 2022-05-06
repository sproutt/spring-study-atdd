package codesquad.service;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

	public List<Question> findAll(){
		return questionRepository.findAll();
	}

	public Question findQuestionById(Long id){
		return questionRepository.findById(id).filter(question -> !question.isDeleted()).orElseThrow(EntityNotFoundException::new);
	}

}
