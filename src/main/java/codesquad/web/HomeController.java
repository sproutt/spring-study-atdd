package codesquad.web;

import codesquad.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final QuestionService questionService;

    public HomeController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "home";
    }
}
