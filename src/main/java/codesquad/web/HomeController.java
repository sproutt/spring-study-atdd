package codesquad.web;

import codesquad.security.HttpSessionUtils;
import codesquad.service.QnaService;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private QnaService qnaService;

    public HomeController(QnaService qnaService){
        this.qnaService = qnaService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("questions", qnaService.findAll());
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);

        return "redirect:/";
    }
}
