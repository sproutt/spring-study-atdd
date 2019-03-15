package codesquad.web;

import codesquad.domain.User;
import codesquad.exception.UnAuthorizedException;
import codesquad.security.HttpSessionUtils;
import codesquad.security.LoginUser;
import codesquad.service.QuestionService;
import codesquad.domain.QuestionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "questionService")
    private QuestionService questionService;

    @GetMapping("/form")
    public String form(HttpSession httpSession) {
        if (HttpSessionUtils.isLoginUser(httpSession)) {
            return "/qna/form";
        }
        throw new UnAuthorizedException("");
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "/home";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, QuestionDto questionDto) {
        questionService.create(loginUser, questionDto);
        return "redirect:/questions";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("questions", questionService.findById(id));
        return "/qna/show";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser, QuestionDto questionDto, @PathVariable Long id) {
        questionService.update(loginUser, questionDto, id);
        return "redirect:/questions/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser, @PathVariable Long id) {
        questionService.delete(loginUser, id);
        return "redirect:/questions";
    }
}
