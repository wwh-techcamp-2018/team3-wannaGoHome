package wannagohome.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.user.User;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardHeaderDto {

    private String boardTitle;

    private String teamTitle;

    private List<User> members;

    public static BoardHeaderDto valueOf(Board board) {
        return new BoardHeaderDto(board.getTitle(), board.getTeam().getName(), null);
    }
}
