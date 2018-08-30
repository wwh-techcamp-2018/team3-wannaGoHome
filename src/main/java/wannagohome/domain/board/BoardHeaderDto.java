package wannagohome.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserIncludedInBoard;
import wannagohome.domain.user.UserPermission;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardHeaderDto {

    private String boardTitle;

    private String teamTitle;

    private boolean deleted;

    private List<User> members;

    private UserPermission permission;

    public static BoardHeaderDto valueOf(Board board) {
        return new BoardHeaderDto(
                board.getTitle(),
                board.getTeam().getName(),
                board.isDeleted(),
                null,
                null
        );
    }

    public static BoardHeaderDto valueOf(Board board, UserIncludedInBoard userIncludedInBoard) {
        return new BoardHeaderDto(
                board.getTitle(),
                board.getTeam().getName(),
                board.isDeleted(),
                null,
                userIncludedInBoard.getPermission()
        );
    }

    public static BoardHeaderDto valueOf(Board board, List<User> members, UserIncludedInBoard userIncludedInBoard) {
        return new BoardHeaderDto(
                board.getTitle(),
                board.getTeam().getName(),
                board.isDeleted(),
                members,
                userIncludedInBoard.getPermission()
        );
    }
}
