package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    @Override
    public Object[] getArguments() {
        return new Object[]{
                team.getName(),
                source.getName(),
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

}
