package codesquad.web;

import codesquad.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QnaService qnaService;

    @GetMapping("/questions/form")
    public String form() {
        return "/qna/form";
    }
}
