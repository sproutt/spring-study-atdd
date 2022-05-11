package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/questions")
public class QuestionController {
    public static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    private final QnaService qnaService;

    public QuestionController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("/form")
    public String form(@LoginUser User loginUser) {
        log.debug("form() loginUser ={}", loginUser);
        return "qna/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, Question question) {
        log.debug("create() loginUser ={}", loginUser);
        log.debug("create() question ={}", question);
        qnaService.create(loginUser, question);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id, @LoginUser User loginUser) throws CannotDeleteException {
        log.debug("remove() loginUser ={}", loginUser);
        qnaService.deleteQuestion(loginUser, id);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, @LoginUser User loginUser, Model model) {
        log.debug("show() loginUser ={}", loginUser);
        Question question = qnaService.findById(id);
        model.addAttribute("question", question);
        return "qna/show";
    }

    @GetMapping("/updateForm")
    public String updateForm(@LoginUser User loginUser) {
        log.debug("updateForm() loginUser ={}", loginUser);
        return "qna/updateForm";
    }


    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @LoginUser User loginUser, Question question) {
        log.debug("update() loginUser ={}", loginUser);
        log.debug("update() question ={}", question);
        qnaService.update(loginUser, id, question);
        return "redirect:/";
    }
}