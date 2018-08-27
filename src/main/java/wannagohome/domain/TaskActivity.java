package wannagohome.domain;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("TaskActivity")
@NoArgsConstructor
public class TaskActivity extends AbstractActivity {

    @ManyToOne
    private Task task;

    private TaskActivity(User source, Task task, ActivityType activityType) {
        this.source = source;
        this.task = task;
        this.type = activityType;
    }

    public static TaskActivity valueOf(User source, Task task, ActivityType activityType) {
        return  new TaskActivity(source, task, activityType);
    }

    @Override
    public Object[] getArguments() {
        return new Object[]{
                this.source.getName(),
                this.getTeam().getName(),
                this.getBoard().getTitle(),
                this.task.getTitle()
        };
    }

    @Override
    public Board getBoard() {
        return task.getBoard();
    }

    @Override
    public Team getTeam() {
        return task.getBoard().getTeam();
    }
}
