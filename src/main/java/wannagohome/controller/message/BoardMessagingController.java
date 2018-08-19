package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import wannagohome.domain.*;
import wannagohome.interceptor.HttpHandshakeInterceptor;
import wannagohome.repository.BoardRepository;
import wannagohome.repository.TaskRepository;
import wannagohome.service.BoardService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class BoardMessagingController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private BoardService boardService;

    // headerAccessor maintains link to session
    @MessageMapping("/message/board")
    @SendTo("/topic/board")
    public BoardDto updateBoard(@Payload String message, SimpMessageHeaderAccessor headerAccessor, BoardDto boardDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());
        return boardService.findById(1L).getBoardDto();
    }

    @MessageMapping("/message/add/task")
    @SendTo("/topic/board")
    public BoardDto addTaskToBoard(@Payload String message, SimpMessageHeaderAccessor headerAccessor, TaskDto taskDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());

        return boardService.addBoardTask(1L, new Task(taskDto)).getBoardDto();
    }

    @MessageMapping("/message/reorder/task")
    @SendTo("/topic/board")
    public BoardDto reorderTasks(@Payload String message, SimpMessageHeaderAccessor headerAccessor, TaskOrderDto taskOrderDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());

        return boardService.reorderBoardTasks(1L, taskOrderDto).getBoardDto();
    }
}
