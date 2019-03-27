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

	@Test
	public void delete_fail_question_not_exist(){

	}

	@Test
	public void delete_fail_different_user(){

	}

	@Test
	public void delete_success_same_user_with_no_answers(){

	}

	@Test
	public void delete_success_same_user_with_same_answers_writer(){

	}

	@Test
	public void delete_fail_same_user_but_different_answers_writer(){

	}
}
