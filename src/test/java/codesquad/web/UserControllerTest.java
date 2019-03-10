package codesquad.web;

import codesquad.domain.User;
import codesquad.domain.UserRepository;
import codesquad.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    UserService userService;

    private UserController userController;

    @Before
    public void setUp(){
        userController = new UserController(userService);
    }

    @Test
    public void create()throws Exception{
        User user = new User("javajigi","password","name","");
        userController.create(user);

        verify(userService).add(user);
    }


}