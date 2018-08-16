package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardDto {

    private String title;
//    private List<User> members;
    private List<Task> tasks;
//    private List<Activity> activities;

//    private Color color;

}
