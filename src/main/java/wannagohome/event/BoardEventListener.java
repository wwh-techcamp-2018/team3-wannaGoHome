package wannagohome.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.BoardActivity;
import wannagohome.domain.User;
import wannagohome.domain.UserIncludedInTeam;
import wannagohome.repository.ActivityRepository;
import wannagohome.repository.UserIncludedInBoardRepository;
import wannagohome.repository.UserIncludedInTeamRepository;

@Component
public class BoardEventListener implements ApplicationListener<BoardEvent> {

    @Autowiredgit
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMessageGenerator activityMessageGenerator;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    private static final Logger log = LoggerFactory.getLogger(BoardEventListener.class);

    @Override
    public void onApplicationEvent(BoardEvent event) {
        BoardActivity activity = event.getActivity();
        userIncludedInTeamRepository.findAllByTeam(activity.getTeam())
                .forEach(userIncludedInTeam -> handleEvent(activity, userIncludedInTeam.getUser()));
    }

    private void handleEvent(BoardActivity activity, User user) {
        activity.setReceiver(user);
        saveActivity(activity);
        sendMessage(activity);
    }

    private void saveActivity(BoardActivity activity) {
        activity.setId(null);
        activityRepository.save(activity);
    }

    private void sendMessage(BoardActivity activity) {
        simpMessageSendingOperations.convertAndSend("/topic/activity/team/" + activity.getBoard().getTeam().getId(), activityMessageGenerator.generateMessage(activity));
    }
}
