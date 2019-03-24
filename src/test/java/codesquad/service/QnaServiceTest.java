package codesquad.service;

import codesquad.domain.*;
import codesquad.exception.UnAuthorizedException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static codesquad.domain.UserTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    private MockMvc mvc;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private DeleteHistoryService deleteHistoryService;

    @InjectMocks
    private QnaService qnaService;

    public Question question1;
    public Answer answer1;

    @BeforeClass
    public void init(){
        question1 = new Question("title", "context");
        question1.setId(1);
        question1.writeBy(JAVAJIGI);
        question1.addAnswer(new Answer(1L ,SANJIGI,question1, "answer title1"));
        question1.addAnswer(new Answer(2L ,JAVAJIGI,question1, "answer title2"));

        answer1 = new Answer(3L ,JAVAJIGI,question1, "answer title3");
    }

    @Test
    public void delete_question_with_answers(){
        when(questionRepository.findById(question1.getId())).thenReturn(Optional.of(question1));
        when(questionRepository.save(question1)).thenReturn(question1);

        Question deletedQuestion = qnaService.deleteQuestionWithAnswer(JAVAJIGI, question1.getId());

        assertThat(deletedQuestion.isDeleted()).isEqualTo(true);
        assertThat(deletedQuestion.getAnswers().stream().filter(answer-> !answer.isDeleted())).isNull();
    };

    @Test(expected = UnAuthorizedException.class)
    public void delete_question_with_answers_unmatch_id(){
        Question deletedQuestion = qnaService.deleteQuestionWithAnswer(SANJIGI, question1.getId());
    }

    @Test
    public void add_answer(){
        when(questionRepository.findById(question1.getId())).thenReturn(Optional.of(question1));
        when(questionRepository.save(question1)).thenReturn(question1);

        qnaService.addAnswer(JAVAJIGI, question1.getId(), "add content");

        assertThat(question1.getAnswers().stream().filter(answer-> answer.getContents().equals("add content"))).isNotNull();
    }

    @Test
    public void delete_answer(){
        when(answerRepository.findById(answer1.getId())).thenReturn(Optional.of(answer1));

        Answer deletedAnswer=qnaService.deleteAnswer(JAVAJIGI, answer1.getId());

        assertThat(deletedAnswer.isDeleted()).isEqualTo(true);

    }

}
