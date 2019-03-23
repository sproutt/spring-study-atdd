package codesquad.service;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.exception.QuestionDeletedException;
import codesquad.exception.QuestionNotFoundException;
import codesquad.domain.QuestionDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service("questionService")
public class QuestionService {

    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public List<Question> findAllNotDeleted(){
        return questionRepository.findAll().stream().filter(question -> !question.isDeleted()).collect(Collectors.toList());
    }

    public Question create(User loginUser, QuestionDTO questionDto) {
        return questionRepository.save(new Question(questionDto.getTitle(), questionDto.getContent()));
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    public Question update(User loginUser, QuestionDTO questionDto, Long id) {
        return questionRepository.save(findById(id).update(loginUser, questionDto));
    }

    public void delete(User loginUser, Long id) {
        questionRepository.save(findById(id).delete(loginUser));
    }

    public Question findByIdNotDeleted(Long id) {
        return questionRepository.findById(id).filter(question -> !question.isDeleted()).orElseThrow(QuestionDeletedException::new);
    }
}
