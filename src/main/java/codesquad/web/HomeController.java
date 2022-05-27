package codesquad.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import codesquad.domain.Question;
import codesquad.service.DeleteHistoryService;
import codesquad.service.QuestionService;

import static codesquad.domain.ContentType.*;

@Controller
public class HomeController {

    @Resource(name = "questionService")
    private QuestionService questionService;

    @GetMapping("/")
    public String home(Model model) {
        List<Question> questions = questionService.findAll();
        List<Question> nonDeletedQuestion = new ArrayList<>();
        for (Question question : questions) {
            if (!question.isDeleted()) {
                nonDeletedQuestion.add(question);
            }
        }
        model.addAttribute("questions", nonDeletedQuestion);
        return "home";
    }
}
