package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    @Autowired
    private QnaService qnaService;

    @GetMapping("")
    public Iterable<Answer> list(@PathVariable Long questionId) {
        return qnaService.findAllAnswerNotDeleted(questionId);
    }


    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser, @PathVariable Long questionId, String contents) {
        Answer answer = qnaService.addAnswer(loginUser, questionId, contents);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api"+answer.generateUrl()));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/{answerId}")
    public Answer detail(@PathVariable Long answerId) {
        return qnaService.findAnswerByIdNotDeleted(answerId);
    }

    @DeleteMapping("/{answerId}")
    public Answer delete(@LoginUser User loginUser, @PathVariable Long answerId) {
        return qnaService.deleteAnswer(loginUser, answerId);
    }
}
