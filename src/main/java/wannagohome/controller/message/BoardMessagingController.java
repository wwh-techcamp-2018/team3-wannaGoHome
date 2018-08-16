package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import wannagohome.domain.BoardDto;
import wannagohome.domain.TaskDto;
import wannagohome.interceptor.HttpHandshakeInterceptor;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class BoardMessagingController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    // headerAccessor maintains link to session
    @MessageMapping("/message/board")
    @SendTo("/topic/board")
    public BoardDto updateBoard(@Payload String message, SimpMessageHeaderAccessor headerAccessor, BoardDto boardDto) throws Exception {

//        System.out.println("Received update! " + boardDto.getTitle());
//        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
//        headerAccessor.setSessionId(session.getId());
//        System.out.println(session.getId());
//        return SessionUtil.getBoardInSession(session).getBoardDto();
        System.out.println("Message Received!");
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());
        return SessionUtil.getBoardInSession(session).getBoardDto();
    }

    @MessageMapping("/message/add/task")
    @SendTo("/topic/board")
    public BoardDto addTaskToBoard(@Payload String message, SimpMessageHeaderAccessor headerAccessor, TaskDto taskDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());

        SessionUtil.getBoardInSession(session).addTask(taskDto.toTask());

        return SessionUtil.getBoardInSession(session).getBoardDto();
    }
}
