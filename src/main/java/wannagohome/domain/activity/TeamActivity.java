package wannagohome.domain.activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.board.Board;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserPermission;

import javax.persistence.*;
import java.util.Objects;


@Entity
@DiscriminatorValue("TeamActivity")
@NoArgsConstructor
@Getter
public class TeamActivity extends AbstractActivity {

    @ManyToOne
    private Team team;

    @ManyToOne
    private User target;

    @Enumerated(EnumType.STRING)
    private UserPermission permission;

    private TeamActivity(User source, Team team, ActivityType activityType, User target, UserPermission permission) {
        this.source = source;
        this.type = activityType;
        this.target = target;
        this.permission = permission;
        this.team = team;
    }

    public static TeamActivity valueOf(User source, Team team, ActivityType activityType, User target, UserPermission permission) {
        return new TeamActivity(source, team, activityType, target, permission);
    }

    public static TeamActivity valueOf(User source, Team team, ActivityType activityType) {
        return new TeamActivity(source, team, activityType, null, null);
    }

    public static TeamActivity valueOf(User source, Team team, ActivityType activityType, User target) {
        return new TeamActivity(source, team, activityType, target, null);
    }

    @Override
    public Object[] getArguments() {
        return new Object[]{
                source.getName(),
                team.getName(),
                Objects.isNull(target) ? "" : target.getName(),
                Objects.isNull(permission) ? "" : permission.name()
        };
    }

    @Override
    public Board getBoard() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Team getTeam() {
        return team;
    }

    public String getLink() {
        if (ActivityType.TEAM_MEMBER_REMOVE == type || ActivityType.TEAM_DELETE == type)
            return "/";
        if (ActivityType.TEAM_MEMBER_INVITE == type)
            return "/users/profile";
        return "/team/" + getTeam().getId();
    }
}

