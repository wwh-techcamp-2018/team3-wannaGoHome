package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.User;
import wannagohome.repository.ActivityRepository;

@Component
public class ActivityEventHandler {


    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ActivityMessageGenerator activityMessageGenerator;

    @Autowired
    private ActivityRepository activityRepository;


    public void handleEvent(AbstractActivity activity, User user) {
        activity.setReceiver(user);
        saveActivity(activity);
        sendMessage(activity);
    }

    private void saveActivity(AbstractActivity activity) {
        activity.setId(null);
        activityRepository.save(activity);
    }

    private void sendMessage(AbstractActivity activity) {
        // TODO: 2018. 8. 22. MessageDto 만들기
        simpMessageSendingOperations.convertAndSend(activity.getTopicUrl(), activityMessageGenerator.generateMessage(activity));
    }
}
