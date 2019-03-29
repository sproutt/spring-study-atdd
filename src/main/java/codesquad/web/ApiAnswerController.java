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

    @PostMapping("")
    public ResponseEntity<Void> post(@LoginUser User loginUser, @PathVariable Long questionId, @RequestBody String contents) {
        Answer savedAnswer = qnaService.addAnswer(loginUser, questionId, contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api" + savedAnswer.generateUrl()));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public Answer delete(@LoginUser User loginUser, @PathVariable Long id) {
        return qnaService.deleteAnswer(loginUser, id);
    }
}
