package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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

    @GetMapping("/questions/{id}")
    public String showQuestion(Model model, @PathVariable long id) {
        model.addAttribute("question", qnaService.findById(id).get());
        return "/qna/show";
    }

    @GetMapping("/questions/{id}/updateForm")
    public String updateForm(Model model, @PathVariable long id) {
        model.addAttribute("question", qnaService.findById(id).get());
        return "/qna/updateForm";
    }

    @PutMapping("/questions/{id}")
    public String update(@LoginUser User loginUser, @PathVariable long id, Question updatedQuestion) {
        try {
            qnaService.update(loginUser, id, updatedQuestion);
            return "redirect:" + updatedQuestion.generateUrl();
        } catch (UnAuthenticationException e) {
            return "redirect:/";
        }
    }
}
