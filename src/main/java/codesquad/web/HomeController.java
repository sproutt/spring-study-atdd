package codesquad.web;

import codesquad.service.QnaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final QnaService qnaService;

    public HomeController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("questions", qnaService.findAll());
        return "home";
    }
}
