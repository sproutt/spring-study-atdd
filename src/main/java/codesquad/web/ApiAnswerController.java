package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    @Autowired
    private QnaService qnaService;

    @GetMapping("")
    public Iterable<Answer> list(@PathVariable Long questionId){
        return qnaService.findAllAnswerNotDeleted(questionId);
    }


    @PostMapping("")
    public Answer create(@LoginUser User loginUser, @PathVariable Long questionId, String contents){
        return qnaService.addAnswer(loginUser, questionId, contents);
    }

    @GetMapping("/{answerId}")
    public Answer detail(@PathVariable Long answerId){
        return qnaService.findAnswerByIdNotDeleted(answerId);
    }

    @DeleteMapping("/{answerId}")
    public Answer delete(@LoginUser User loginUser , @PathVariable Long answerId){
        return qnaService.deleteAnswer(loginUser, answerId);
    }
}
