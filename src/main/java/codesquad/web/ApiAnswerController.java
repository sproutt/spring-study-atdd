package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class ApiAnswerController {
    public static final Logger log = LoggerFactory.getLogger(ApiAnswerController.class);

    private final QnaService qnaService;

    public ApiAnswerController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @PostMapping("/{question_id}/answers")
    public Answer create(@LoginUser User user, @PathVariable("question_id") long questionId, @RequestBody String contents) {
        log.info("user = {}", user);
        log.info("question-id = {}", questionId);
        log.info("contents = {}", contents);

        return qnaService.addAnswer(user, questionId, contents);
    }
}
