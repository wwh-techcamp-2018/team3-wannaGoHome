package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardDto {

    private String title;
//    private List<User> members;
    private List<TaskDto> tasks;
//    private List<Activity> activities;

//    private Color color;

}
