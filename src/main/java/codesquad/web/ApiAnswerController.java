package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {
    private static final Logger log = LoggerFactory.getLogger(ApiAnswerController.class);

    private final QnaService qnaService;

    public ApiAnswerController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @PostMapping("")
    public ResponseEntity<Answer> create(@LoginUser User loginUser, @PathVariable Long questionId, @Valid @RequestBody String contents) {
        Answer savedAnswer = qnaService.addAnswer(loginUser, questionId, contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + questionId + "/answers/" + savedAnswer.getId()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Answer> update(@LoginUser User loginUser, @PathVariable Long id, @Valid @RequestBody String updatedContents) {
        return new ResponseEntity<>(qnaService.updateAnswer(loginUser, id, updatedContents), HttpStatus.OK);
    }
}
