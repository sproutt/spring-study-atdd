package codesquad.service;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class QnaServiceTest {

	@InjectMocks
	private QnaService qnaService;

	@Mock
	private QuestionRepository questionRepository;

	private User user;
	private Question question;

	@Before
	public void setUp() {
		user = new User("TestId", "password", "name", "email");
		question = new Question("title", "contents");
		questionRepository.save(question);
	}

	@Test
	public void create() {
		Question question = new Question();
		when(questionRepository.save(question)).thenReturn(question);
		assertThat(qnaService.create(user, question).getWriter().equals(user));
	}

	@Test
	public void update() {
		question.setId(1L);
		question.writeBy(user);
		when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
		Question updatedQuestion = qnaService.update(user, 1L, question);
		assertThat(qnaService.update(user, 1L, updatedQuestion).getTitle().equals("title"));
		assertThat(qnaService.update(user, 1L, updatedQuestion).getContents().equals("contents"));
	}

	@Test
	public void delete() throws Exception {
		question.setId(1L);
		question.writeBy(user);
		when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
		qnaService.deleteQuestion(user, 1L);
		assertThat(question.isDeleted()).isTrue();
	}
}
