package wannagohome.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.board.Board;
import wannagohome.domain.board.ChatMessage;
import wannagohome.domain.board.ChatMessageDto;
import wannagohome.domain.user.User;
import wannagohome.repository.ChatMessageRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private BoardService boardService;

    public List<ChatMessage> getRecentMessagesFromBoard(Board board) {
        List<ChatMessage> messages = chatMessageRepository.findFirst40ByBoardEqualsOrderByMessageCreatedDesc(board);
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
