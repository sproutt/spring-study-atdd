package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import codesquad.web.dto.QuestionDto;
import codesquad.web.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QnaService qnaService;
    private final QuestionMapper questionMapper;


    @GetMapping("/questions/form")
    public String form() {
        return "/qna/form";
    }

    @PostMapping("/questions")
    public String create(@LoginUser User loginUser, Question question) {
        log.info("loginUser = {}", loginUser.getUserId());
        log.info("question = {}", question.getContents());

        qnaService.create(loginUser, question);
        return "redirect:/";
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("questions", qnaService.findAll());
        return "/home";
    }

    @GetMapping("/questions/{id}")
    public String showQuestion(Model model, @PathVariable long id) {
        model.addAttribute("question", qnaService.findById(id).get());
        return "/qna/show";
    }

    @GetMapping("/questions/{id}/updateForm")
    public String updateForm(Model model, @PathVariable long id) {
        model.addAttribute("question", qnaService.findById(id).get());
        return "/qna/updateForm";
    }

    @PutMapping("/questions/{id}")
    public String update(@LoginUser User loginUser, @PathVariable long id, QuestionDto updatedQuestionDto, Model model) {
        try {
            model.addAttribute("question", qnaService.update(loginUser, id, questionMapper.toEntity(updatedQuestionDto)));
            return "redirect:/questions/" + id;
        } catch (UnAuthenticationException e) {
            return "redirect:/";
        }
    }

    @DeleteMapping("/questions/{id}")
    public String delete(@LoginUser User loginUser, @PathVariable long id) {
        try {
            qnaService.deleteQuestion(loginUser, id);
            return "redirect:/";
        } catch (CannotDeleteException exception) {
            return "redirect:/questions/" + id;
        }
    }
}
