package wannagohome.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.board.*;
import wannagohome.domain.card.Card;
import wannagohome.domain.user.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.BoardService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@Api(value = "Board", description = "board 관리 API")
public class ApiBoardController {

    @Autowired
    private BoardService boardService;

    @ApiOperation(value = "main page의 board summary 가져오기")
    @ApiImplicitParam(name = "boardSummary", value = "boardSummaryDto", required = true,  paramType = "json")
    @GetMapping("")
    public BoardSummaryDto boardSummary(@LoginUser User user) {
        return boardService.getBoardSummary(user);
    }

    @ApiOperation(value = "board 생성시 필요한 board info 생성")
    @ApiImplicitParam(name = "createBoardInfo", value = "CreateBoardInfoDto", required = true,  paramType = "json")
    @GetMapping("/createBoardInfo")
    public CreateBoardInfoDto createBoardInfo(@LoginUser User user) {
        return boardService.getCreateBoardInfo(user);
    }


    @ApiOperation(value = "새 board 생성하기")
    @ApiImplicitParam(name = "createBoard", value = "Board 정보를 요약한Card Dto", required = true,  paramType = "json")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BoardCardDto createBoard(@LoginUser User user, @RequestBody  @Valid CreateBoardDto createBoardDto) {
        return BoardCardDto.valueOf(boardService.createBoard(user, createBoardDto));
    }


    @ApiOperation(value = "board의 member 가져오기")
    @ApiImplicitParam(name = "getBoardMembers", value = "Member의 리스트", required = true,  paramType = "json")
    @GetMapping("/{boardId}/members")
    public List<User> getBoardMembers(@RequestParam String keyword) {
        return null;
    }

    @ApiOperation(value = "Board 내부의 카드 중 Due Date가 존재하는 카드 가져오기")
    @ApiImplicitParam(name = "getCardsExistDueDate", value = "due date가 존재하는 카드 리스트", required = true,  paramType = "json")
    @GetMapping("/{boardId}/cards")
    @ResponseStatus(HttpStatus.OK)
    public List<Card> getCardsByDueDate(@PathVariable Long boardId) {
        return boardService.findCardsByDueDate(boardId);
    }

    @ApiOperation(value = "Board 초기 상태 가져오기")
    @ApiImplicitParam(name = "getBoardState", value = "초기 board Dto", required = true,  paramType = "json")
    @GetMapping("/{boardId}")
    public BoardInitDto getBoardState(@LoginUser User user, @PathVariable Long boardId) {
        return boardService.getBoardInitInfo(user, boardId);
    }

    @ApiOperation(value = "Board 삭제")
    @ApiImplicitParam(name = "deleteBoard", value = "BoardHeaderDto", required = true,  paramType = "json")
    @DeleteMapping("/{boardId}")
    public BoardHeaderDto deleteBoard(@LoginUser User user, @PathVariable Long boardId) {
        return boardService.deleteBoard(user, boardId);
    }

    @ApiOperation(value = "Board 이름 변경")
    @ApiImplicitParam(name = "renameBoard", value = "BoardHeaderDto", required = true,  paramType = "json")
    @PutMapping("/{boardId}/name")
    public BoardHeaderDto renameBoard(@LoginUser User user, @PathVariable Long boardId, @RequestBody BoardHeaderDto dto) {
        return boardService.renameBoard(user, boardId, dto);
    }
}
