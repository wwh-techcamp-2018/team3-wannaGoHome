package wannagohome.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wannagohome.domain.ChatMessage;
import wannagohome.domain.ChatMessageDto;
import wannagohome.domain.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.BoardService;
import wannagohome.service.ChatMessageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ApiChatController {

    private static final Logger log = LoggerFactory.getLogger(ApiChatController.class);

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private BoardService boardService;

    @GetMapping("/getRecent/{boardId}")
    public List<ChatMessageDto> getRecentMessages(@PathVariable Long boardId, @LoginUser User currentUser) {
        List<ChatMessage> messages = chatMessageService.getRecentMessagesFromBoard(boardService.findById(boardId));
        return messages.stream().map(message -> message.getChatMessageDto()).collect(Collectors.toList());
    }

    @GetMapping("/getRecent/{boardId}/before/{messageOrder}")
    public List<ChatMessageDto> getMessagesBefore(@PathVariable Long boardId, @PathVariable Long messageOrder, @LoginUser User currentUser) {
        List<ChatMessage> messages = chatMessageService.getRecentMessagesBefore(boardService.findById(boardId), messageOrder);
        return messages.stream().map(message -> message.getChatMessageDto()).collect(Collectors.toList());
    }
}
