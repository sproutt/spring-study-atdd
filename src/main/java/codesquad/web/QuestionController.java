package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    public static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QnaService qnaService;

    @GetMapping("/form")
    public String form(@LoginUser User loginUser) {
        return "/qna/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, Question question) {
        qnaService.create(loginUser, question);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id, @LoginUser User loginUser) throws CannotDeleteException {
        qnaService.deleteQuestion(loginUser, id);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, @LoginUser User loginUSer, Model model) {
        Question question = qnaService.findById(id)
                                      .orElseThrow(NoSuchElementException::new);

        model.addAttribute("question", question);
        return "qna/show";
    }

    @GetMapping("/updateForm")
    public String updateForm(@LoginUser User loginUser) {
        return "qna/updateForm";
    }


    @PutMapping("/{id}")
    public String update(@PathVariable Long id, @LoginUser User loginUser, Question question) {
        qnaService.update(loginUser, id, question);
        return "redirect:/";
    }
}