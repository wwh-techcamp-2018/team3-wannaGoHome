package wannagohome.controller.message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import wannagohome.domain.Board;

@Controller
public class BoardMessagingController {

    @MessageMapping("/message/board")
    @SendTo("/message/board")
    public Board board() throws Exception {

        return new Board();
    }
}
