package codesquad.service;

import codesquad.domain.QuestionRepository;
import codesquad.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QnaServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QnaService qnaService;

   @Test
    public void updateQuestion_sucess() throws Exception{

   }

   @Test
    public void updateQuestion_failed_when_no_login() throws Exception{

   }

   @Test
    public void updateQuestion_failed_when_other_user() throws Exception{

   }

   @Test
    public void deleteQuestion_success() throws Exception{

   }

   @Test
    public void deleteQuestion_failed_when_no_login() throws Exception{

   }

    @Test
    public void deleteQuestion_failed_when_other_user() throws Exception{

    }

    @Test
    public void addAnswer_success() throws Exception{

    }

    @Test
    public void addAnswer_failed_when_no_login() throws Exception{

    }

    @Test
    public void deleteAnswer_success() throws Exception{

    }

    @Test
    public void deleteAnswer_failed_when_no_login() throws Exception{

    }

    @Test
    public void deleteAnswer_failed_when_other_user() throws Exception{

    }
}
