package wannagohome.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wannagohome.domain.board.ChatMessage;
import wannagohome.domain.board.ChatMessageDto;
import wannagohome.domain.user.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.BoardService;
import wannagohome.service.ChatMessageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@Api(value = "Chat", description = "Chatting 관리 API")
public class ApiChatController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private BoardService boardService;

    @ApiOperation(value = "최근 채팅 메세지 가져오기")
    @ApiImplicitParam(name = "getRecentMessages", value = "ChatMessageDto 리스트", required = true,  paramType = "json")
    @GetMapping("/getRecent/{boardId}")
    public List<ChatMessageDto> getRecentMessages(@PathVariable Long boardId, @LoginUser User currentUser) {
        List<ChatMessage> messages = chatMessageService.getRecentMessagesFromBoard(boardService.findById(boardId));
        return messages.stream().map(message -> message.getChatMessageDto()).collect(Collectors.toList());
    }

    @ApiOperation(value = "이전 채팅 메세지 가져오기")
    @ApiImplicitParam(name = "getMessagesBefore", value = "ChatMessageDto 리스트", required = true,  paramType = "json")
    @GetMapping("/getRecent/{boardId}/before/{messageOrder}")
    public List<ChatMessageDto> getMessagesBefore(@PathVariable Long boardId, @PathVariable Long messageOrder, @LoginUser User currentUser) {
        List<ChatMessage> messages = chatMessageService.getRecentMessagesBefore(boardService.findById(boardId), messageOrder);
        return messages.stream().map(message -> message.getChatMessageDto()).collect(Collectors.toList());
    }
}
