package wannagohome.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Board {

    private String title;
    private List<User> members;
    private List<Task> tasks;
    private List<Activity> activities;

    private Color color;

    private boolean deleted;

    public Board() {

    }

    public Board(String title, List<User> members, List<Task> tasks, List<Activity> activities, Color color) {
        this.title = title;
        this.members = members;
        this.tasks = tasks;
        this.activities = activities;
        this.color = color;
    }


}
