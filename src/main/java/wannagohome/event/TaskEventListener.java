package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.TaskActivity;
import wannagohome.repository.UserIncludedInBoardRepository;

@Component
public class TaskEventListener implements ApplicationListener<TaskEvent> {

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    ActivityEventHandler activityEventHandler;

    @Override
    public void onApplicationEvent(TaskEvent event) {
        TaskActivity activity = event.getTaskActivity();
        userIncludedInBoardRepository.findAllByBoard(activity.getBoard())
                .forEach(userIncludedInBoard -> activityEventHandler.handleEvent(activity, userIncludedInBoard.getUser()));
    }
}
