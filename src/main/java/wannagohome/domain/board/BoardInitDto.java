package wannagohome.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wannagohome.domain.task.Task;
import wannagohome.domain.task.TaskDto;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserPermission;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardInitDto {

    private Long id;
    private String boardTitle;
    private String teamTitle;

    private List<User> members;
    private List<TaskDto> tasks;

    private Color color;

    private UserPermission permission;

    public static BoardInitDto valueOf(Board board, List<User> members, UserPermission permission) {
        return new BoardInitDto(
                board.getId(),
                board.getTitle(),
                board.getTeam().getName(),
                members,
                board.getTasks().stream().map(Task::getTaskDto).collect(Collectors.toList()),
                board.getColor(),
                permission
        );
    }
}
