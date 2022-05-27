package codesquad.service;

import codesquad.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class QuestionServiceTest {
	@Mock
	private QuestionRepository questionRepository;
	@Mock
	private DeleteHistoryService deleteHistoryService;
	@InjectMocks
	private QuestionService questionService;

	private Question question;
	private Question updateQuestion;
	private User user;

	@Before
	public void setUp() throws Exception {
		user = new User("jiny", "1234", "jiny", "jinyeong@gmail.com");
		question = new Question("new", "new content");
		question.writeBy(user);
		updateQuestion = new Question("update", "update content");
		updateQuestion.writeBy(user);
	}

	@Test
	public void create(){
		Question newQuestion = new Question("new title", "new contents");
		questionService.create(user, newQuestion);
		verify(questionRepository, times(1)).save(newQuestion);
	}

	@Test
	public void findAll() {
		when(questionRepository.findAll()).thenReturn(Collections.singletonList(question));
		assertEquals(1, questionService.findAll().size());
	}

	@Test
	public void update() {
		updateQuestion.writeBy(user);
		when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
		questionService.updateQuestion(user, anyLong(), updateQuestion);
	}

	@Test
	public void delete() throws Exception {
		when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question));
		questionService.deleteQuestion(user, anyLong());
	}

}