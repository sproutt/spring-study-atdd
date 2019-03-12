package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QuestionController {

	@Autowired
	private QnaService qnaService;

	@GetMapping("/form")
	public String form() {
		return "/qna/form";
	}

	@PostMapping("")
	public String post(@LoginUser User loginUser, Question question) {
		qnaService.create(loginUser, question);
		return "redirect:/";
	}
}
