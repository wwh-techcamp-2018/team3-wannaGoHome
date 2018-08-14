package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wannagohome.domain.SignInDto;
import wannagohome.domain.SignUpDto;
import wannagohome.domain.User;
import wannagohome.service.UserService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public User signUp(@RequestBody @Valid SignUpDto dto) {
        return userService.signUp(dto);
    }

    @PostMapping("/signin")
    public User signIn(HttpSession session, @RequestBody @Valid SignInDto dto) {
        User user = userService.signIn(dto);
        SessionUtil.setUserSession(session, user);
        return user;
    }

}
