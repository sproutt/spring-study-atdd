package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionDTO;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import codesquad.service.QuestionService;
import org.aspectj.weaver.ast.ITestVisitor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {

    @Resource(name="qnaService")
    private QnaService qnaService;

    @GetMapping("")
    public Iterable<Question> list(){
        return qnaService.findAllNotDeleted();
    }

    @GetMapping("/{id}")
    public Question detail(@PathVariable Long id){
        return qnaService.findByIdNotDeleted(id);
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser, @Valid @RequestBody QuestionDTO questionDTO){
        Question question=qnaService.create(loginUser, questionDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/"+question.getId()));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Question update(@LoginUser User loginUser , @Valid @RequestBody QuestionDTO questionDTO, @PathVariable Long id){
        Question question= qnaService.update(loginUser, questionDTO, id);
        return question;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@LoginUser User loginUser, @PathVariable Long id){
        qnaService.delete(loginUser, id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions"));
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }


}
