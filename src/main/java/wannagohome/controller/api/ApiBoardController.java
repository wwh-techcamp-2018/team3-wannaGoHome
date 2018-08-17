package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wannagohome.domain.BoardSummaryDto;
import wannagohome.domain.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.BoardService;

@RestController
@RequestMapping("/api/boards")
public class ApiBoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("")
    public BoardSummaryDto boardSummary(@LoginUser User user) {
        return boardService.getBoardSummary(user);
    }
}
