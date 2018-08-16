package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import wannagohome.domain.BoardDto;
import wannagohome.interceptor.HttpHandshakeInterceptor;

@Controller
public class BoardMessagingController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/message/board")
    @SendTo("/topic/board")
    public BoardDto updateBoard(@Payload String message, SimpMessageHeaderAccessor headerAccessor, BoardDto boardDto) throws Exception {

        System.out.println("Received update! " + boardDto.getTitle());
        String sessionId = headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID).toString();
        headerAccessor.setSessionId(sessionId);

        return boardDto;
    }
}
