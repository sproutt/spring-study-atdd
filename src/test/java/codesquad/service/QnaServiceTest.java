package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.is;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    private User user;
    private Question question;

    @Before
    public void setUp() throws Exception {
        user = new User("sanjigi", "password", "name", "javajigi@slipp.net");
        question = new Question("title", "this is test");
        question.writeBy(user);
        when(questionRepository.findById(question.getId())).thenReturn(Optional.of(question));
    }

    @Test
    public void create_question() throws Exception {

        qnaService.create(question.getWriter(), question);
        assertThat(qnaService.findById(question.getId()).get().getContents(), is(question.getContents()));
    }

    @Test(expected = UnAuthenticationException.class)
    public void update_failed_when_wrong_user() {
        User wrongUser = new User("bell", "1111", "named", "ww@gmail.com");
        Question updateQuestion = new Question("hihi", "bye");

        qnaService.update(wrongUser, question.getId(), updateQuestion);
    }

    @Test(expected = CannotDeleteException.class)
    public void delete_failed_when_wrong_user() throws CannotDeleteException {
        User wrongUser = new User("bell", "1111", "named", "ww@gmail.com");
        qnaService.deleteQuestion(wrongUser, question.getId());
    }

    @Test
    public void update_question() {
        qnaService.update(user, question.getId(), new Question("updateTitle", "this is update"));
        assertThat(qnaService.findById(question.getId()).get().getContents(), is("this is update"));
    }

    @Test
    public void delete_question() throws CannotDeleteException {
        qnaService.deleteQuestion(user, question.getId());
        assertThat(qnaService.findById(question.getId()).get().isDeleted(), is(question.isDeleted()));
    }
}
