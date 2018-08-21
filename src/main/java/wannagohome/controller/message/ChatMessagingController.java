package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import wannagohome.domain.ChatMessageDto;
import wannagohome.domain.User;
import wannagohome.interceptor.HttpHandshakeInterceptor;
import wannagohome.service.ChatMessageService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class ChatMessagingController {

    @Autowired
    private ChatMessageService chatMessageService;


    @MessageMapping("/message/board/{boardId}/chat")
    @SendTo("/topic/board/{boardId}/chat")
    public ChatMessageDto sendChatMessage(@Payload String message, @DestinationVariable Long boardId,
                                          SimpMessageHeaderAccessor headerAccessor, ChatMessageDto chatMessageDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        User currentUser = SessionUtil.getUserSession(session);
        return chatMessageService.createMessage(boardId, chatMessageDto, currentUser).getChatMessageDto(currentUser);
    }



}
