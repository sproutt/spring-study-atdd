package codesquad.web;

import codesquad.domain.Question;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiQuestionController {
    public static final Logger log = LoggerFactory.getLogger(ApiQuestionController.class);

    private final QnaService qnaService;

    public ApiQuestionController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("/questions/{id}")
    public Question read(@PathVariable long id) {
        return qnaService.findById(id);
    }
}
