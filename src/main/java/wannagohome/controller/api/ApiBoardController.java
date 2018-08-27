package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.*;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.BoardService;

import javax.validation.Valid;
import java.util.List;

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
    public BoardCardDto createBoard(@LoginUser User user, @RequestBody  @Valid CreateBoardDto createBoardDto) {
        return BoardCardDto.valueOf(boardService.createBoard(user, createBoardDto));
    }

    @GetMapping("/{boardId}/members")
    public List<User> getBoardMembers(@RequestParam String keyword) {
        return null;
    }

    @GetMapping("/{boardId}/cards")
    @ResponseStatus(HttpStatus.OK)
    public List<Card> getCardsByDueDate(@PathVariable Long boardId) {
        return boardService.findCardsByDueDate(boardId);
    }

    @GetMapping("/{boardId}")
    public BoardDto getBoardState(@PathVariable Long boardId) {
        return boardService.findById(boardId).getBoardDto();
    }
}
