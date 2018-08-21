package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardDto {

    private Long id;
    private String title;
//    private List<User> members;
    private List<TaskDto> tasks;
//    private List<AbstractActivity> activities;

//    private Color color;

}
