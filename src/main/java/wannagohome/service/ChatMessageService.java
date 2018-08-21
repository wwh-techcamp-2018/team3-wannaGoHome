package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.Board;
import wannagohome.domain.ChatMessage;
import wannagohome.domain.ChatMessageDto;
import wannagohome.repository.ChatMessageRepository;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private BoardService boardService;

    public List<ChatMessage> getRecentMessagesFromBoard(Board board) {
        return chatMessageRepository.findFirst20ByBoardEqualsOrderByMessageCreatedDesc(board);
    }

    public ChatMessage createMessage(Long boardId, ChatMessageDto chatMessageDto, HttpSession session) {
        ChatMessage message = new ChatMessage(chatMessageDto);

        message.setOrder(chatMessageRepository.countChatMessagesByBoardEquals(boardService.findById(boardId)));
        message.setAuthor(SessionUtil.getUserSession(session));
        return chatMessageRepository.save(message);
    }

}
