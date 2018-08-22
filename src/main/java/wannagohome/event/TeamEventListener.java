package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.TeamActivity;
import wannagohome.repository.UserIncludedInTeamRepository;

@Component
public class TeamEventListener implements ApplicationListener<TeamEvent> {

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    ActivityEventHandler activityEventHandler;
    @Override
    public void onApplicationEvent(TeamEvent event) {
        TeamActivity activity = event.getActivity();
        userIncludedInTeamRepository.findAllByTeam(activity.getTeam())
                .forEach(userIncludedInTeam -> activityEventHandler.handleEvent(activity, userIncludedInTeam.getUser()));
    }
}
