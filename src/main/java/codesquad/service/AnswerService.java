package codesquad.service;

import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public Answer save(User loginUser, Long questionId, Answer answer) {
        answer.toQuestion(questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("question not found")));
        answer.writeBy(loginUser);

        return answerRepository.save(answer);
    }

    public Optional<Answer> findById(Long id) {
        return answerRepository.findById(id);
    }
}
