package wannagohome.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping("/signin")
    public String signIn() {
        return "/users/signin";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "/users/signup";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        SessionUtil.removeUserSession(session);
        return "redirect:/users/signin";
    }
}
