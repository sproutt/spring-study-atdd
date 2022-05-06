package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.UnAuthorizedException;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.UserService;
import codesquad.web.dto.LoginUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "userService")
    private UserService userService;

    @GetMapping("/form")
    public String form() {
        return "/user/form";
    }

    @PostMapping("")
    public String create(User user) {
        userService.add(user);
        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model) {
        List<User> users = userService.findAll();
        log.debug("user size : {}", users.size());
        model.addAttribute("users", users);
        return "/user/list";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, @PathVariable long id, Model model) {
        model.addAttribute("user", userService.findById(loginUser, id));
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser, @PathVariable long id, User target) {
        userService.update(loginUser, id, target);
        return "redirect:/users";
    }

    @PostMapping("/login")
    public String login(LoginUserDto loginUserDto) {
        log.info("login userId = {}", loginUserDto.getUserId());
        log.info("login password = {}", loginUserDto.getPassword());

        try {
            User savedUser = userService.login(loginUserDto.getUserId(), loginUserDto.getPassword());
            return "redirect:/users";
        } catch (UnAuthenticationException e) {
            return "/user/login_failed";
        }
    }
}
