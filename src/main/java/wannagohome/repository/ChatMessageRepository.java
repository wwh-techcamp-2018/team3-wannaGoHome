package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.board.Board;
import wannagohome.domain.board.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findFirst40ByBoardEqualsOrderByMessageCreatedDesc(Board board);

    List<ChatMessage> findFirst20ByMessageOrderBeforeAndBoardEqualsOrderByMessageOrderDesc(Long messageOrder, Board board);

    long countChatMessagesByBoardEquals(Board board);
}
