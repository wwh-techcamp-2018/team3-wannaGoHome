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
//    private List<User> members;
    private List<TaskDto> tasks;
//    private List<AbstractActivity> activities;

    private Color color;

}
