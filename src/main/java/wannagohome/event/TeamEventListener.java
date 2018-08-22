package wannagohome.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.TeamActivity;
import wannagohome.repository.UserIncludedInTeamRepository;

@Component
public class TeamEventListener implements ApplicationListener<TeamEvent> {

    private static final Logger log = LoggerFactory.getLogger(TeamEventListener.class);

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    ActivityEventHandler activityEventHandler;
    @Override
    public void onApplicationEvent(TeamEvent event) {
        AbstractActivity activity = event.getActivity();
        userIncludedInTeamRepository.findAllByTeam(activity.getTeam())
                .forEach(userIncludedInTeam -> activityEventHandler.handleEvent(activity, userIncludedInTeam.getUser()));
    }
}
