package wannagohome.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.User;
import wannagohome.repository.ActivityRepository;

import javax.annotation.Resource;

@Component
public class ActivityEventHandler {
    private static final Logger log = LoggerFactory.getLogger(ActivityEventHandler.class);

    @Resource(name = "biDirectionEncoder")
    private PasswordEncoder encoder;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ActivityMessageGenerator activityMessageGenerator;

    @Autowired
    private ActivityRepository activityRepository;


    public void handleEvent(AbstractActivity activity, User user) {
        log.debug("handleEvent is called");
        activity.setReceiver(user);
        saveActivity(activity);
        sendMessage(activity);
    }

    private void saveActivity(AbstractActivity activity) {
        AbstractActivity saveActivity = (AbstractActivity) activity.clone();
        saveActivity.clearId();
        activityRepository.save(saveActivity);
    }

    private void sendMessage(AbstractActivity activity) {
        log.debug("sendMessage is called with Activity: {} {}", activity.getCode(), activity.getReceiver());
        simpMessageSendingOperations.convertAndSend(
                activity.getTopic(encoder),
                activityMessageGenerator.generateMessage(activity)
        );
    }
}
