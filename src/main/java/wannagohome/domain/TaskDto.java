package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskDto {

//    private User author;
    private String title;
    private List<Card> cards;

    private boolean deleted;

    // TODO: task 쪽으로
    public Task toTask() {
        return new Task(title);
    }

}