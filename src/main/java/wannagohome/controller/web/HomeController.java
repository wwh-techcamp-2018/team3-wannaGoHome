package wannagohome.controller.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wannagohome.domain.User;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(User user, Model model) {
        return "index";
    }
}
