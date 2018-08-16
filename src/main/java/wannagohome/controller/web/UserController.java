package wannagohome.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
