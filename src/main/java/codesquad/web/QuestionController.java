package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QnaService qnaService;

    @GetMapping("/questions/form")
    public String form() {
        return "/qna/form";
    }

    @PostMapping("/questions")
    public String create(@LoginUser User loginUser, Question question) {
        log.info("loginUser = {}", loginUser.getUserId());
        log.info("question = {}", question.getContents());

        qnaService.create(loginUser, question);
        return "redirect:/";
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("questions", qnaService.findAll());
        return "/home";
    }
}
