package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/questions/{id}/answer")
public class ApiAnswerController {

    @PostMapping("")
    public ResponseEntity<Void> post(@LoginUser User loginUser, @RequestBody Answer answer) {
        Answer savedAnswer = answerService.save(answer);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api" + savedAnswer.generateUrl());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}
