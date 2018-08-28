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

    private List<User> members;

    private UserPermission permission;

    public static BoardHeaderDto valueOf(Board board, UserIncludedInBoard userIncludedInBoard) {
        return new BoardHeaderDto(board.getTitle(), board.getTeam().getName(), null, userIncludedInBoard.getPermission());
    }
}
