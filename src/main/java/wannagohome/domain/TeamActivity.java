package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@DiscriminatorValue("TeamActivity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TeamActivity extends AbstractActivity {

    @ManyToOne
    private Team team;

    @ManyToOne
    private User target;

    @Enumerated(EnumType.STRING)
    private UserPermission permission;

    public static TeamActivity valueOf(User source, Team team, ActivityType type, User target) {
        TeamActivity activity = new TeamActivity();
        activity.source = source;
        activity.team = team;
        activity.type = type;
        activity.target = target;
        return activity;
    }

    @Override
    public Object[] getArguments() {
        return new Object[]{
                target.getName(),
                permission.name()
        };
    }

    @Override
    public String getTopicUrl() {
        return "/topic/activity/team/" + team.getId();
    }

    @Override
    public String getSubscribeTopicUrl() {
        if(ActivityType.TEAM_MEMBER_ADD == type) {
            return "/topic/activity/team/" + team.getId();
        }
        return super.getSubscribeTopicUrl();
    }
}
