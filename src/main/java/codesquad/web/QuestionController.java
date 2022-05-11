package codesquad.web;

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
    public String questionForm(@LoginUser User loginUser) {
        return "/qna/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, Question question) {
        Question savedQuestion = qnaService.create(loginUser, question);
        return "redirect:" + savedQuestion.generateUrl();
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("question", qnaService.findById(id));
        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, @PathVariable Long id, Model model) {
        model.addAttribute("question", qnaService.findById(id));
        log.debug("question : {}", qnaService.findById(id));
        return "/qna/updateForm";
    }

    @PutMapping("{id}")
    public String update(@LoginUser User loginUser, @PathVariable Long id, Question question) {
        Question updatedQuestion = qnaService.update(loginUser, id, question);
        return "redirect:/" + updatedQuestion.generateUrl();
    }
}
