package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.*;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.BoardService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/boards")
public class ApiBoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("")
    public BoardSummaryDto boardSummary(@LoginUser User user) {
        return boardService.getBoardSummary(user);
    }

    @GetMapping("/createBoardInfo")
    public CreateBoardInfoDto createBoardInfo(@LoginUser User user) {
        return boardService.getCreateBoardInfo(user);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Board createBoard(@LoginUser User user, @RequestBody  @Valid CreateBoardDto createBoardDto) {
        return boardService.createBoard(user, createBoardDto);
    }
}
