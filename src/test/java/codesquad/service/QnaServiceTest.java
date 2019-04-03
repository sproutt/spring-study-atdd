package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.domain.*;
import codesquad.dto.AnswerDTO;
import codesquad.dto.QuestionDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QnaService qnaService;

    private User user;
    private User newUser;
    private Question question;
    private Answer answer;
    private Answer newAnswer;

    @Before
    public void setUp() {
        user = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        newUser = new User("bellroute", "1111", "bell", "bell@gmail.com");
        question = new Question("title", "this is test");
        question.writeBy(user);
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));

        answer = new Answer(user, "this is answer");
        newAnswer = new Answer(newUser, "this is newAnswer");
        when(answerRepository.findById(answer.getId())).thenReturn(Optional.of(answer));
    }

    @Test
    public void create_question() {
        qnaService.create(question.getWriter(), new QuestionDTO(question.getTitle(), question.getContents()));
        assertThat(qnaService.findById(question.getId()).getContents(), is(question.getContents()));
    }


    @Test
    public void update_question() {
        qnaService.update(user, question.getId(), new QuestionDTO("updateTitle", "this is update"));
        assertThat(qnaService.findById(question.getId()).getContents(), is("this is update"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_question_failed_when_no_login() {
        QuestionDTO updateQuestion = new QuestionDTO("hihi", "this is update");
        qnaService.update(null, question.getId(), updateQuestion);
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_question_failed_when_wrong_login() {
        QuestionDTO updateQuestion = new QuestionDTO("hihi", "this is update");
        qnaService.update(newUser, question.getId(), updateQuestion);
    }

    @Test
    public void delete_question() throws Exception {
        qnaService.deleteQuestion(user, question.getId());
        assertThat(qnaService.findById(question.getId()).isDeleted(), is(question.isDeleted()));
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_question_failed_when_wrong_user() throws Exception {
        qnaService.deleteQuestion(newUser, question.getId());
    }

    @Test(expected = Exception.class)
    public void delete_question_failed_when_no_login() throws Exception {
        qnaService.deleteQuestion(null, question.getId());
    }


    @Test(expected = CannotDeleteException.class)
    public void delete_question_failed_when_others_answers() throws Exception {
        question.addAnswer(newAnswer);
        qnaService.deleteQuestion(user, question.getId());
    }

    @Test
    public void create_answer() {
        qnaService.addAnswer(user, question.getId(), new AnswerDTO(answer.getContents()));
        assertThat(qnaService.findAnswerById(answer.getId()).getContents(), is(answer.getContents()));
    }

    @Test
    public void update_answer() {
        qnaService.updateAnswer(user, question.getId(), new AnswerDTO("this is update"));
        assertThat(qnaService.findAnswerById(answer.getId()).getContents(), is("this is update"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_answer_failed_when_wrong_user() {
        qnaService.updateAnswer(newUser, question.getId(), new AnswerDTO("this is update"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_answer_failed_when_no_login() {
        qnaService.updateAnswer(null, question.getId(), new AnswerDTO("this is update"));
    }

    @Test
    public void delete_answer() throws CannotDeleteException {
        qnaService.deleteAnswer(user, answer.getId());
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_answer_failed_when_wrong_user() throws CannotDeleteException {
        qnaService.deleteAnswer(newUser, answer.getId());
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_answer_failed_when_no_login() throws CannotDeleteException {
        qnaService.deleteAnswer(null, answer.getId());
    }
}
