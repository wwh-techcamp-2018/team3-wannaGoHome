package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import wannagohome.domain.*;
import wannagohome.interceptor.HttpHandshakeInterceptor;
import wannagohome.service.BoardService;
import wannagohome.service.TaskService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class BoardMessagingController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private BoardService boardService;

    @Autowired
    private TaskService taskService;

    // headerAccessor maintains link to session
    @MessageMapping("/message/board/{boardId}")
    @SendTo("/topic/board/{boardId}")
    public BoardDto getBoardState(@DestinationVariable Long boardId,
                                  SimpMessageHeaderAccessor headerAccessor) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());
        return boardService.findById(boardId).getBoardDto();
    }

    @MessageMapping("/message/add/{boardId}/task")
    @SendTo("/topic/board/{boardId}")
    public BoardDto addTaskToBoard(@DestinationVariable Long boardId,
                                   SimpMessageHeaderAccessor headerAccessor, TaskDto taskDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());
        taskDto.setAuthor(SessionUtil.getUserSession(session));
        return boardService.addBoardTask(boardId, new Task(taskDto)).getBoardDto();
    }

    @MessageMapping("/message/reorder/{boardId}/task")
    @SendTo("/topic/board/{boardId}")
    public BoardDto reorderTasks( @DestinationVariable Long boardId,
                                 SimpMessageHeaderAccessor headerAccessor, TaskOrderDto taskOrderDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());

        return boardService.reorderBoardTasks(boardId, taskOrderDto).getBoardDto();
    }

    @MessageMapping("/message/add/{boardId}/{taskId}/card")
    @SendTo("/topic/board/{boardId}")
    public BoardDto addCardToBoard(@DestinationVariable Long taskId, SimpMessageHeaderAccessor headerAccessor, CardDto cardDto) throws Exception {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        headerAccessor.setSessionId(session.getId());
        cardDto.setAuthor(SessionUtil.getUserSession(session));
        Card card = new Card(cardDto);
        Task task = taskService.addCard(taskId, card);
        return task.getBoard().getBoardDto();
    }

    @MessageMapping("/message/reorder/{boardId}/{taskId}/card")
    @SendTo("/topic/board/{boardId}")
    public BoardDto reorderCard(@DestinationVariable Long taskId, CardOrderDto cardOrderDto) throws Exception {
        Task task = taskService.reorderTaskCard(taskId, cardOrderDto);
        return task.getBoard().getBoardDto();
    }
}
