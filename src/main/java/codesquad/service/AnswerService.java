package codesquad.service;

import codesquad.UnAuthorizedException;
import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    private final QuestionService questionService;

    public Answer createAnswer(User loginUser, long questionId, String contents) {
        Question question = questionService.findById(questionId);
        Answer answer = new Answer(loginUser, contents);
        answer.addToQuestion(question);

        return answerRepository.save(answer);
    }

    @Transactional
    public Answer deleteAnswer(User user, long answerId) {
        Answer savedAnswer = answerRepository.findById(answerId)
                                             .filter(answer -> answer.isOwner(user))
                                             .orElseThrow(UnAuthorizedException::new);


        return savedAnswer.delete();
    }

    public Answer findByAnswerId(long answerId) {
        return answerRepository.findById(answerId)
                               .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Answer updateAnswer(User user, long answerId, String updatedContents) {

        Answer savedAnswer = answerRepository.findById(answerId)
                                             .filter(answer -> answer.isOwner(user))
                                             .orElseThrow(UnAuthorizedException::new);

        return savedAnswer.updateContents(updatedContents);
    }
}
