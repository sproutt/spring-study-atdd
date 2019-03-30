package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDTO;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {
    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("/{id}")
    public Question show(@PathVariable long id) {
        return qnaService.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser, @RequestBody QuestionDTO questionDTO) {
        Question createdQuestion = qnaService.create(loginUser, questionDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + createdQuestion.getId()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Question update(@LoginUser User loginUser, @PathVariable long id, @RequestBody QuestionDTO questionDTO) {
        return qnaService.update(loginUser, id, questionDTO);
    }
}
