package wannagohome.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class BoardController {

    @GetMapping("/board/{id}")
    public String board(HttpSession session, @PathVariable Long id) {
        // TODO: Need to handle user accessibility to board!
        return "/board";
    }

}
