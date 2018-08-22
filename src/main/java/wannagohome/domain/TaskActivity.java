package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("TaskActivity")
@NoArgsConstructor
@AllArgsConstructor
public class TaskActivity extends AbstractActivity {

    @ManyToOne
    private Task task;

    public static TaskActivity valueOf(User source, Task task, ActivityType type) {
        TaskActivity activity = new TaskActivity();
        activity.source = source;
        activity.task = task;
        activity.type = type;
        return activity;
    }

    @Override
    public Object[] getArguments() {
        return new Object[]{this.task.getTitle()};
    }

    public Board getBoard() {
        return task.getBoard();
    }

    @Override
    public String getTopicUrl() {
        return "/topic/activity/board/" + getBoard().getId();
    }
}
