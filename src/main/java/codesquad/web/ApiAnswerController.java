package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.dto.AnswerDTO;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("")
    public Answer show(@PathVariable long questionId) {
        return qnaService.findAnswerById(questionId);
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser, @PathVariable long questionId, @RequestBody AnswerDTO answerDTO) {
        Answer createdAnswer =  qnaService.addAnswer(loginUser, questionId, answerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(String.format("/api/questions/%d", questionId) + createdAnswer.getId()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Answer update(@LoginUser User loginUser, @PathVariable long questionId, @PathVariable long id, @RequestBody AnswerDTO answerDTO) {
        return qnaService.updateAnswer(loginUser, id, answerDTO);
    }

    @DeleteMapping("/{id}")
    public Answer delete(@LoginUser User loginUser, @PathVariable long questionId, @PathVariable long id) throws CannotDeleteException {
        return qnaService.deleteAnswer(loginUser, id);
    }
}