package wannagohome.domain.board;

import lombok.Getter;
import lombok.Setter;
import wannagohome.domain.task.TaskDto;

import java.util.List;

@Getter
@Setter
public class BoardDto {

    private Long id;
    private String title;
    private List<TaskDto> tasks;
    private Color color;

}
