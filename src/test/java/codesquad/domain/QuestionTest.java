package codesquad.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionTest {

	User user = new User();
	Question question = new Question();

	@Test
	public void update() {
		String title = "title";
		String contents = "contents";
		Question updatedQuestion = new Question(title, contents);
		question.writeBy(user);
		question.update(user, updatedQuestion);
		assertThat(question.getTitle().equals(title));
		assertThat(question.getContents().equals(contents));
	}

	@Test
	public void delete() {
		question.writeBy(user);
		question.delete(user);
		assertThat(question.isDeleted()).isTrue();
	}

}
