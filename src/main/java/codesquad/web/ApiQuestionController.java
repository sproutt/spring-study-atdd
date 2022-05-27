package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiQuestionController {
    public static final Logger log = LoggerFactory.getLogger(ApiQuestionController.class);

    private final QnaService qnaService;

    public ApiQuestionController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @PostMapping("/questions")
    public Question create(@LoginUser User user, @RequestBody Question question) {
        log.debug("질문 = {}", question);
        return qnaService.create(user, question);
    }

    @GetMapping("/questions/{id}")
    public Question read(@PathVariable long id) {
        return qnaService.findById(id);
    }

    @PutMapping("/questions/{id}")
    public Question update(@LoginUser User user, @PathVariable long id, @RequestBody Question updatedQuestion) {
        log.debug("수정요청 된 질문 ={}", updatedQuestion);
        return qnaService.update(user, id, updatedQuestion);
    }

    @DeleteMapping("/questions/{id}")
    public void delete(@LoginUser User user, @PathVariable long id) throws CannotDeleteException {
        qnaService.deleteQuestion(user, id);
    }
}
