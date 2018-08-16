package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import wannagohome.domain.Board;
import wannagohome.domain.BoardDto;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class BoardMessagingController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/message/board")
    @SendTo("/topic/board")
    public Board updateBoard(HttpSession session, BoardDto boardDto) throws Exception {

        System.out.println("Received update!" + boardDto.getTitle());
        SessionUtil.setBoardInSession(session);
        return new Board();
    }
}
