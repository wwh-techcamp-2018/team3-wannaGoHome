package wannagohome.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class BoardController {

    @GetMapping("/board")
    public String board(HttpSession session) {
        return "/board";
    }

}
