package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.User;
import codesquad.dto.QuestionDTO;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        model.addAttribute("question", qnaService.findById(id));

        return "/qna/show";
    }

    @GetMapping("/form")
    public String form() {
        return "/qna/form";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, String title, String contents) {
        qnaService.create(loginUser, new QuestionDTO(title, contents));

        return "redirect:/users";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, @PathVariable long id, Model model) {
        model.addAttribute("question", qnaService.findById(loginUser, id));

        return "/qna/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser, @PathVariable long id, QuestionDTO questionDTO) {
        qnaService.update(loginUser, id, questionDTO);

        return String.format("redirect:/questions/%d", id);
    }

    @DeleteMapping("{id}")
    public String delete(@LoginUser User loginUser, @PathVariable long id) throws Exception {
        qnaService.deleteQuestion(loginUser, id);

        return "redirect:/users";
    }
}
