package wannagohome.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.BoardActivity;
import wannagohome.repository.UserIncludedInTeamRepository;

@Component
public class BoardEventListener implements ApplicationListener<BoardEvent> {

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    ActivityEventHandler activityEventHandler;

    private static final Logger log = LoggerFactory.getLogger(BoardEventListener.class);

    @Override
    public void onApplicationEvent(BoardEvent event) {
        BoardActivity activity = event.getActivity();
        userIncludedInTeamRepository.findAllByTeam(activity.getTeam())
                .forEach(userIncludedInTeam -> activityEventHandler.handleEvent(activity, userIncludedInTeam.getUser()));
    }


}
