package wannagohome.event;

import org.springframework.context.ApplicationEvent;
import wannagohome.domain.ActivityType;
import wannagohome.domain.Team;
import wannagohome.domain.TeamActivity;
import wannagohome.domain.User;

public class TeamEvent extends ApplicationEvent {

    private TeamActivity activity;

    public TeamEvent(Object object, User source, Team team, ActivityType type, User target) {
        super(object);
        activity = TeamActivity.valueOf(source, team, type, target);
    }
}
