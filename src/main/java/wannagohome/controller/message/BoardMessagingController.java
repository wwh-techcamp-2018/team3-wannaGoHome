package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import wannagohome.domain.board.BoardDto;
import wannagohome.domain.card.Card;
import wannagohome.domain.card.CardDto;
import wannagohome.domain.card.CardOrderDto;
import wannagohome.domain.task.Task;
import wannagohome.domain.task.TaskDto;
import wannagohome.domain.task.TaskOrderDto;
import wannagohome.domain.user.User;
import wannagohome.interceptor.HttpHandshakeInterceptor;
import wannagohome.service.BoardService;
import wannagohome.service.TaskService;
import wannagohome.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class BoardMessagingController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private BoardService boardService;

    @Autowired
    private TaskService taskService;

    @Resource(name = "biDirectionEncoder")
    private PasswordEncoder encoder;

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

    @MessageMapping("/message/rename/{boardId}/{taskId}")
    @SendTo("/topic/board/{boardId}")
    public BoardDto renameTask(@DestinationVariable Long boardId, TaskDto taskDto) throws Exception {
        taskService.renameTask(taskDto);
        return boardService.findById(boardId).getBoardDto();
    }

    @MessageMapping("/message/delete/{boardId}/{taskId}")
    @SendTo("/topic/board/{boardId}")
    public BoardDto deleteTask(@DestinationVariable Long boardId, TaskDto taskDto) throws Exception {
        taskService.deleteTask(taskDto);
        return boardService.findById(boardId).getBoardDto();
    }

    @MessageMapping("/message/reorder/{boardId}/task")
    @SendTo("/topic/board/{boardId}")
    public BoardDto reorderTasks(@DestinationVariable Long boardId,
                                 SimpMessageHeaderAccessor headerAccessor, TaskOrderDto taskOrderDto) throws Exception {

        return boardService.reorderBoardTasks(boardId, taskOrderDto).getBoardDto();
    }

    @MessageMapping("/message/add/{boardId}/{taskId}/card")
    @SendTo("/topic/board/{boardId}")
    public BoardDto addCardToBoard(@DestinationVariable Long taskId, SimpMessageHeaderAccessor headerAccessor, CardDto cardDto) throws Exception {
        User user = SessionUtil.getUserSession(headerAccessor);
        cardDto.setAuthor(user);
        Card card = new Card(cardDto);
        Task task = taskService.addCard(user, taskId, card);
        return task.getBoard().getBoardDto();
    }

    @MessageMapping("/message/reorder/{boardId}/{taskId}/card")
    @SendTo("/topic/board/{boardId}")
    public BoardDto reorderCard(@DestinationVariable Long taskId, CardOrderDto cardOrderDto) throws Exception {
        Task task = taskService.reorderTaskCard(taskId, cardOrderDto);
        return task.getBoard().getBoardDto();
    }

    @MessageMapping("/boards/{boardId}/expel/init")
    @SendTo("/topic/boards/expel/init")
    public String sendTopicForExpel(@DestinationVariable Long boardId, SimpMessageHeaderAccessor headerAccessor) {
        User user = SessionUtil.getUserSession(headerAccessor);
        return String.format("/topic/boards/%d/%s", boardId, user.encodedCode(encoder));
    }
}
