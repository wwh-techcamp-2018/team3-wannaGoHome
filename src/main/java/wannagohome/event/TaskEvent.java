package wannagohome.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wannagohome.domain.ActivityType;
import wannagohome.domain.Task;
import wannagohome.domain.TaskActivity;
import wannagohome.domain.User;

public class TaskEvent extends ApplicationEvent {

    @Getter
    private TaskActivity taskActivity;

    public TaskEvent(Object object, User source, Task task, ActivityType type) {
        super(object);
        taskActivity = TaskActivity.valueOf(source, task, type);
    }
}
