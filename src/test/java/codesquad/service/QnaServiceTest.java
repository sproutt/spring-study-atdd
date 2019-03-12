package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
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

	private User user, user2;
	private Question question, updatedQuestion;
	private Long id;

	@BeforeClass
	public void setUpBeforeClass() {
		User user = new User(1l, "esp2ar0", "test", "changhwan", "esp2ar0@gmail.com");
		User user2 = new User(2l, "burrito", "test", "chicken", "burrito@gmail.com");
	}

	@Before
	public void setUp() {
		question = new Question("title1", "contents1");
		updatedQuestion = new Question("title2", "contents2");
		id = 1l;
		question.setId(id);
		question.writeBy(user);
	}

	@Test
	public void update_success() {
		//when
		when(questionRepository.findById(id)).thenReturn(Optional.of(question));
		when(questionRepository.save(question)).thenReturn(question);

		//then
		assertThat(qnaService.update(user, id, updatedQuestion).getTitle()).isEqualTo(updatedQuestion.getTitle());
	}

	@Test(expected = UnAuthorizedException.class)
	public void update_failed_when_mismatch_writer() {
		//when
		when(questionRepository.findById(id)).thenReturn(Optional.of(question));
		qnaService.update(user2, id, updatedQuestion);

		//then throw UnAuthorizedException
	}

	@Test
	public void delete_success() throws CannotDeleteException {
		//when
		when(questionRepository.findById(id)).thenReturn(Optional.of(question));
		qnaService.deleteQuestion(user, id);

		//then
		assertThat(qnaService.findById(id).get().isDeleted()).isTrue();
	}

	@Test(expected = CannotDeleteException.class)
	public void delete_failed() throws CannotDeleteException {
		//when
		when(questionRepository.findById(id)).thenReturn(Optional.of(question));
		qnaService.deleteQuestion(user2, id);

		//then throw CannotDeleteException
	}
}
