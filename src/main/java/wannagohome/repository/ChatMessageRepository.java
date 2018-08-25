package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Board;
import wannagohome.domain.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findFirst20ByBoardEqualsOrderByMessageCreatedDesc(Board board);
    List<ChatMessage> findFirst20ByMessageOrderBeforeAndBoardEqualsOrderByMessageOrderDesc(Long messageOrder, Board board);
    long countChatMessagesByBoardEquals(Board board);
}
