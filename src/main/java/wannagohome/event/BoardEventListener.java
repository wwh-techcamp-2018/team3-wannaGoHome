package wannagohome.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.Activity;
import wannagohome.domain.BoardActivity;
import wannagohome.domain.UserIncludedInTeam;
import wannagohome.repository.ActivityRepository;
import wannagohome.repository.UserIncludedInBoardRepository;
import wannagohome.repository.UserIncludedInTeamRepository;

@Component
public class BoardEventListener implements ApplicationListener<BoardEvent> {

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private ActivityRepository activityRepository;

    private static final Logger log = LoggerFactory.getLogger(BoardEventListener.class);

    @Override
    public void onApplicationEvent(BoardEvent event) {
        // TODO: 2018. 8. 21. AbstractActivity DB  저장
        BoardActivity activity = event.getActivity();
        BoardActivity saveActivity;
        userIncludedInTeamRepository.findAllByTeam(activity.getBoard().getTeam())
                .forEach(userIncludedInTeam -> handleEvent(activity, userIncludedInTeam));
    }

    private void handleEvent(BoardActivity activity, UserIncludedInTeam userIncludedInTeam) {
        activity.setReceiver(userIncludedInTeam.getUser());
        saveActivity(activity);
        sendMessage(activity);
    }

    private void saveActivity(BoardActivity activity) {
        activity.setId(null);
        activityRepository.save(activity);
    }

    private void sendMessage(BoardActivity activity) {
        // TODO: 2018. 8. 21. 웹 소켓 전송
    }
}
