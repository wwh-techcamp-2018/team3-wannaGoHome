package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
    @MessageMapping("/message/board/{boardId}")
    @SendTo("/topic/board/{boardId}")
    public BoardDto getBoardState(@DestinationVariable Long boardId,
                                  SimpMessageHeaderAccessor headerAccessor) throws Exception {
        return boardService.findById(boardId).getBoardDto();
    }

    @MessageMapping("/message/add/{boardId}/task")
    @SendTo("/topic/board/{boardId}")
    public BoardDto addTaskToBoard(@DestinationVariable Long boardId,
                                   SimpMessageHeaderAccessor headerAccessor, TaskDto taskDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION);
        taskDto.setAuthor(SessionUtil.getUserSession(session));

        return boardService.addBoardTask(boardId, new Task(taskDto)).getBoardDto();
    }

    @MessageMapping("/message/reorder/{boardId}/task")
    @SendTo("/topic/board/{boardId}")
    public BoardDto reorderTasks(@DestinationVariable Long boardId,
                                 SimpMessageHeaderAccessor headerAccessor, TaskOrderDto taskOrderDto) throws Exception {


        return boardService.reorderBoardTasks(boardId, taskOrderDto).getBoardDto();
    }
}
