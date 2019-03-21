package codesquad.service;

import codesquad.UnAuthorizedException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

    private User user;
    private User anotherUser;
    private Question question;

    @Before
    public void setUp() {
        user = new User("TestId", "password", "name", "email");
        user.setId(1L);
        anotherUser = new User("anotherUserId", "password", "another", "email");
        anotherUser.setId(2L);
        question = new Question("title", "contents");
        questionRepository.save(question);
        question.setId(1L);
        question.writeBy(user);
        when(questionRepository.findById((long) 1)).thenReturn(Optional.of(question));
    }

    @Test
    public void create() {
        Question question = new Question();
        when(questionRepository.save(question)).thenReturn(question);
        assertThat(qnaService.create(user, question).getWriter().equals(user));
    }

    @Test
    public void update() {
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Question updatedQuestion = qnaService.update(user, 1L, question);
        assertThat(qnaService.update(user, 1L, updatedQuestion).getTitle().equals("title"));
        assertThat(qnaService.update(user, 1L, updatedQuestion).getContents().equals("contents"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_failed() {
        qnaService.update(anotherUser, question.getId(), question);
    }

    @Test
    public void delete() throws Exception {
        qnaService.deleteQuestion(user, 1L);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_failed() throws Exception{
        qnaService.deleteQuestion(anotherUser, 1L);
    }

}
