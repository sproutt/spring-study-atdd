package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private QnaService qnaService;

    public QuestionController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("/form")
    public String showForm() {
        return "qna/form";
    }

    @PostMapping
    public String createQuestion(@LoginUser User loginUser, Question question) {
        qnaService.create(loginUser, question);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String showQuestion(@PathVariable long id,  Model model){
        model.addAttribute("question", qnaService.findById(id));

        return "qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, @PathVariable long id, Model model) {

        model.addAttribute("question", qnaService.findById(id));
        return "qna/updateForm";
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser, @PathVariable long id) throws Exception {
        qnaService.deleteQuestion(loginUser, id);

        return "redirect:/";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser, @PathVariable long id, Question updatedQuestion) throws Exception{
        qnaService.update(loginUser, id, updatedQuestion);

        return "redirect:/";
    }
}
