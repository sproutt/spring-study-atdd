package codesquad.web;

import codesquad.domain.QuestionDTO;
import codesquad.domain.User;
import codesquad.exception.UnAuthorizedException;
import codesquad.security.HttpSessionUtils;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("/form")
    public String form(HttpSession httpSession) {
        if (HttpSessionUtils.isLoginUser(httpSession)) {
            return "/qna/form";
        }
        throw new UnAuthorizedException("");
    }

    @GetMapping("")
    public String list() {
        return "redirect:/";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, QuestionDTO questionDto) {
        qnaService.create(loginUser, questionDto);
        return "redirect:/questions";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("questions", qnaService.findById(id));
        return "/qna/show";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser, QuestionDTO questionDto, @PathVariable Long id) {
        qnaService.update(loginUser, questionDto, id);
        return "redirect:/questions/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser, @PathVariable Long id) {
        qnaService.delete(loginUser, id);
        return "redirect:/questions";
    }
}
