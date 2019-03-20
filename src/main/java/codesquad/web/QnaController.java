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
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QnaController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("")
    public String readList(Model model) {
        List<Question> questions = qnaService.findAll();
        log.debug("question size : {}", questions.size());
        model.addAttribute("questions", questions);
        return "/home";
    }

    @GetMapping("/{id}")
    public String list(@PathVariable long id, Model model) {
        model.addAttribute("question", qnaService.findById(id));
        return "/qna/show";
    }

    @GetMapping("/{id}/show")
    public String updateForm(@PathVariable long id, Model model) {
        model.addAttribute("question", qnaService.findById(id));
        return "/qna/updateForm";
    }

    @PostMapping("")
    public String create(@LoginUser User user, @Valid Question question) {
        qnaService.create(user, question);
        return "redirect:/questions";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User user, @PathVariable long id, @Valid Question question) {
        qnaService.update(user, id, question);
        return "redirect:/questions/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User user, @PathVariable long id) throws Exception {
        qnaService.deleteQuestion(user, id);
        return "redirect:/questions";
    }

}
