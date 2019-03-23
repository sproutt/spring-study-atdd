package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionDTO;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QuestionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/questions")
public class ApiQuestionController {

    @Resource(name="questionService")
    private QuestionService questonServie;

    @GetMapping("")
    public List<Question> list(){
        return questonServie.findAllNotDeleted();
    }

    @GetMapping("/{id}")
    public Question detail(@PathVariable Long id){
        return questonServie.findByIdNotDeleted(id);
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser, @Valid @RequestBody QuestionDTO questionDTO){
        //DTO로 해도되는지 모르겠다.
        Question question=questonServie.create(loginUser, questionDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/"+question.getId()));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Question update(@LoginUser User loginUser , @Valid @RequestBody QuestionDTO questionDTO, @PathVariable Long id){
        Question question= questonServie.update(loginUser, questionDTO, id);
        System.out.println("로그 :" + question.getId() + question.getTitle());
        return question;
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> delete(@LoginUser User loginUser, @PathVariable Long id){
        //DTO로 해도되는지 모르겠다.
        questonServie.delete(loginUser, id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions"));
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }


}
