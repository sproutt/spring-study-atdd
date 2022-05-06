package codesquad.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import codesquad.service.QuestionService;

@Controller
public class HomeController {

    @Resource(name = "questionService")
    private QuestionService questionService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("questions", questionService.findAll());
        return "home";
    }
}
