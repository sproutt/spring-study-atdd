package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("/form")
    public String form(@LoginUser User loginUser) {
        return "/qna/form";
    }

    @PostMapping("")
    public String create(Question question, @LoginUser User loginUser) {
        qnaService.create(loginUser, question);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        model.addAttribute("question", qnaService.findById(id));
        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, @PathVariable long id, Model model) throws Exception {
        Question question = qnaService.ownerCheck(id, loginUser);
        model.addAttribute("question", question);
        return "/qna/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser, @PathVariable long id, Question target) throws Exception {
        qnaService.update(loginUser, id, target);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser, @PathVariable long id) throws Exception {
        qnaService.delete(loginUser, id);
        return "redirect:/";
    }

}
