package wannagohome.controller.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wannagohome.domain.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.BoardService;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    BoardService boardService;
    @GetMapping("/")
    public String home(@LoginUser User user, Model model) {
        model.addAttribute("boardSummary", boardService.getBoardSummary(user));
        log.debug("boardSummary : {}", boardService.getBoardSummary(user));
        return "index";
    }
}
