package codesquad.service;

import codesquad.CannotDeleteException;
import codesquad.UnAuthorizedException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
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

	@Test
	public void update_success() {
		//given
		User user = new User("esp2ar0", "test", "changhwan", "esp2ar0@gmail.com");
		Question question = new Question("title1", "contents1");
		Question updatedQuestion = new Question("title2", "contents2");
		Long id = 1l;
		question.setId(id);
		question.writeBy(user);

		//when
		when(questionRepository.findById(id)).thenReturn(Optional.of(question));

		//then
		assertThat(qnaService.update(user, id, updatedQuestion).getTitle()).isEqualTo(updatedQuestion.getTitle());
	}

	@Test(expected = UnAuthorizedException.class)
	public void update_failed_when_mismatch_writer() {
		//given
		User user = new User(1l, "esp2ar0", "test", "changhwan", "esp2ar0@gmail.com");
		User user2 = new User(2l, "burrito", "test", "chicken", "burrito@gmail.com");
		Question question = new Question("title1", "contents1");
		Question updatedQuestion = new Question("title2", "contents2");
		Long id = 1l;
		question.setId(id);
		question.writeBy(user);

		//when
		when(questionRepository.findById(id)).thenReturn(Optional.of(question));
		qnaService.update(user2, id, updatedQuestion);

		//then throw UnAuthorizedException
	}

	@Test
	public void delete_success() throws CannotDeleteException {
		//given
		User user = new User("esp2ar0", "test", "changhwan", "esp2ar0@gmail.com");
		Question question = new Question("delete-title", "delete-title");
		Long id = 1l;
		question.setId(id);
		question.writeBy(user);

		//when
		when(questionRepository.findById(id)).thenReturn(Optional.of(question));
		qnaService.deleteQuestion(user, id);

		//then
		assertThat(qnaService.findById(id).get().isDeleted()).isTrue();
	}

	@Test(expected = CannotDeleteException.class)
	public void delete_failed() throws CannotDeleteException {
		//given
		User user = new User(1l, "esp2ar0", "test", "changhwan", "esp2ar0@gmail.com");
		User user2 = new User(2l, "burrito", "test", "chicken", "burrito@gmail.com");
		Question question = new Question("delete-title", "delete-title");
		Long id = 1l;
		question.setId(id);
		question.writeBy(user);

		//when
		when(questionRepository.findById(id)).thenReturn(Optional.of(question));
		qnaService.deleteQuestion(user2, id);

		//then throw CannotDeleteException
	}
}
