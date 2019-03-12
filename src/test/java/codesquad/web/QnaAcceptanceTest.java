package codesquad.web;

import codesquad.domain.QuestionRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import support.HtmlFormDataBuilder;
import support.test.AcceptanceTest;

public class QnaAcceptanceTest extends AcceptanceTest {

	@Autowired
	private QuestionRepository questionRepository;

	private HtmlFormDataBuilder formDataBuilder;

	@Before
	public void setUp() {
		HttpEntity<MultiValueMap<String, Object>> request;
	}

	@Test
	public void create() {
	}

	@Test
	public void delete() {

	}

	@Test
	public void update() {

	}

	@Test
	public void read() {

	}

	@Test
	public void list() {

	}

	private void addSampleQuestionData(){
		formDataBuilder
				.addParameter("title","title")
				.addParameter("contents","contents");
	}

}
