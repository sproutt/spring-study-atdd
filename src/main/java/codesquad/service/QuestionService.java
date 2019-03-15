package codesquad.service;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.exception.QuestionNotFoundException;
import codesquad.security.LoginUser;
import codesquad.web.dto.QuestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("questionService")
public class QuestionService {

    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public void create(User loginUser, QuestionDto questionDto) {
        questionRepository.save(new Question(questionDto.getTitle(), questionDto.getContent()));
    }

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
    }

    public void update(User loginUser, QuestionDto questionDto, Long id) {
        questionRepository.save(findById(id).update(loginUser, questionDto));
    }

    public void delete(User loginUser, Long id) {
        findById(id).delete(loginUser);
    }
}
