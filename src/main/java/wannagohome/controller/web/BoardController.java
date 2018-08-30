package wannagohome.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wannagohome.service.BoardService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/board/{id}")
    public String board(HttpSession session, @PathVariable Long id) {
        boardService.viewBoard(id, SessionUtil.getUserSession(session));
        return "/board";
    }

}
