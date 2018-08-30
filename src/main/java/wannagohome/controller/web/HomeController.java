package wannagohome.controller.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wannagohome.service.BoardService;

@Controller
public class HomeController {


    @Autowired
    BoardService boardService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

}
