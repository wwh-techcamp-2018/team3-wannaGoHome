package wannagohome.service;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.Board;
import wannagohome.domain.ChatMessage;
import wannagohome.domain.ChatMessageDto;
import wannagohome.domain.User;
import wannagohome.repository.ChatMessageRepository;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChatMessageService {
    
    private static final Logger log = LoggerFactory.getLogger(ChatMessageService.class);

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private BoardService boardService;

    public List<ChatMessage> getRecentMessagesFromBoard(Board board) {
        List<ChatMessage> messages = chatMessageRepository.findFirst20ByBoardEqualsOrderByMessageCreatedDesc(board);
        return Lists.reverse(messages);
    }

    public List<ChatMessage> getRecentMessagesBefore(Board board, Long messageOrder) {
        List<ChatMessage> messages = chatMessageRepository.findFirst20ByMessageOrderBeforeAndBoardEqualsOrderByMessageOrderDesc(messageOrder, board);
        return Lists.reverse(messages);
    }

    @Transactional
    public ChatMessage createMessage(Long boardId, ChatMessageDto chatMessageDto, User currentUser) {
        ChatMessage message = new ChatMessage(chatMessageDto);
        message.setMessageOrder(chatMessageRepository.countChatMessagesByBoardEquals(boardService.findById(boardId)));
        message.setAuthor(currentUser);
        message.setBoard(boardService.findById(boardId));
        return chatMessageRepository.save(message);
    }

}
