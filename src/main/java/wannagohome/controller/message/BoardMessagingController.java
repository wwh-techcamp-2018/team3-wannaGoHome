package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import wannagohome.domain.BoardDto;
import wannagohome.domain.Task;
import wannagohome.domain.TaskDto;
import wannagohome.domain.TaskOrderDto;
import wannagohome.interceptor.HttpHandshakeInterceptor;
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
    @MessageMapping("/message/board/{boardId}")
    @SendTo("/topic/board/{boardId}")
    public BoardDto getBoardState(@Payload String message, @DestinationVariable Long boardId,
                                  SimpMessageHeaderAccessor headerAccessor) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());
        return boardService.findById(boardId).getBoardDto();
    }

    @MessageMapping("/message/add/{boardId}/task")
    @SendTo("/topic/board/{boardId}")
    public BoardDto addTaskToBoard(@Payload String message, @DestinationVariable Long boardId,
                                   SimpMessageHeaderAccessor headerAccessor, TaskDto taskDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());
        taskDto.setAuthor(SessionUtil.getUserSession(session));

        return boardService.addBoardTask(boardId, new Task(taskDto)).getBoardDto();
    }

    @MessageMapping("/message/reorder/{boardId}/task")
    @SendTo("/topic/board/{boardId}")
    public BoardDto reorderTasks(@Payload String message, @DestinationVariable Long boardId,
                                 SimpMessageHeaderAccessor headerAccessor, TaskOrderDto taskOrderDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());

        return boardService.reorderBoardTasks(boardId, taskOrderDto).getBoardDto();
    }
}
