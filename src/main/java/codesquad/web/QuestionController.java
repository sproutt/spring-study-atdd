package codesquad.web;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QuestionService;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Resource(name = "questionService")
	private QuestionService questionService;

	@Autowired
	public QuestionController(QuestionService questionService){
		this.questionService = questionService;
	}

	@GetMapping("/form")
	public String form() {
		return "/qna/form";
	}

	@PostMapping("")
	public String create(@LoginUser User loginUser, Question question){
		questionService.create(loginUser, question);
		return "redirect:/";
	}

	@GetMapping("/{id}")
	String show(@PathVariable Long id, Model model){
		Question question = questionService.findQuestionById(id);
		model.addAttribute("question", question);
		return "/qna/show";
	}

	@GetMapping("/{id}/form")
	public String edit(@LoginUser User loginUser, @PathVariable Long id, Model model) {
		model.addAttribute("question", questionService.findQuestionById(loginUser, id));
		return "qna/updateForm";
	}

	@PutMapping("/{id}")
	public String update(@LoginUser User loginUser, @PathVariable Long id, Question question) {
		questionService.updateQuestion(loginUser, id, question);
		return "redirect:/questions/{id}";
	}

	@DeleteMapping("/{id}")
	public String delete(@LoginUser User loginUser, @PathVariable Long id) throws CannotDeleteException{
		questionService.deleteQuestion(loginUser, id);
		return "redirect:/";
	}
}
