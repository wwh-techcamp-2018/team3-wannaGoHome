package wannagohome.controller.message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import wannagohome.domain.Board;
import wannagohome.domain.BoardDto;

@Controller
public class BoardMessagingController {

    @MessageMapping("/message/board")
    @SendTo("/topic/board")
    public Board updateBoard(BoardDto boardDto) throws Exception {
        System.out.println("Received update!" + boardDto.getTitle());
        return new Board();
    }
}
