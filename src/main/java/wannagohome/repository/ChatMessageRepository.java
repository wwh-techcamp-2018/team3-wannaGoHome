package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Board;
import wannagohome.domain.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findFirst20ByBoardEqualsOrderByMessageCreatedDesc(Board board);
    Long countChatMessagesByBoardEquals(Board board);
}
