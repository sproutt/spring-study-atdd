package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.AnswerService;
import codesquad.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class ApiAnswerController {
    public static final Logger log = LoggerFactory.getLogger(ApiAnswerController.class);

    private final AnswerService answerService;

    @PostMapping("/{question-id}/answers")
    public Answer create(@LoginUser User user, @PathVariable("question-id") long questionId, @RequestBody String contents) {
        log.info("user = {}", user);
        log.info("question-id = {}", questionId);
        log.info("contents = {}", contents);

        return answerService.createAnswer(user, questionId, contents);
    }

    @GetMapping("/{question-id}/answers/{answer-id}")
    public Answer read(@PathVariable("question-id") long questionId, @PathVariable("answer-id") long answerId) {
        return answerService.findByAnswerId(answerId);
    }

    @PutMapping("/{question-id}/answers/{answer-id}")
    public Answer update(@LoginUser User user, @PathVariable("question-id") long questionId, @PathVariable("answer-id") long answerId, @RequestBody String contents) {
        return answerService.updateAnswer(user, answerId, contents);
    }

    @DeleteMapping("/{question-id}/answers/{answer-id}")
    public Answer delete(@LoginUser User user, @PathVariable("question-id") long questionId, @PathVariable("answer-id") long answerId) {
        return answerService.deleteAnswer(user, answerId);
    }
}
