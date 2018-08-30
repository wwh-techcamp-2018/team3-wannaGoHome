package wannagohome.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.activity.ActivityDto;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamInvite;

import java.util.List;


@Getter
@NoArgsConstructor
public class MyPageDto {
    UserDto user;
    List<Team> teams;
    List<ActivityDto> activities;
    List<TeamInvite> teamInvites;

    private MyPageDto(UserDto user, List<Team> teams, List<ActivityDto> activities, List<TeamInvite> teamInvites) {
        this.user = user;
        this.teams = teams;
        this.activities = activities;
        this.teamInvites = teamInvites;
    }


    public static MyPageDto valueOf(UserDto userDto, List<Team> teams, List<ActivityDto> activities, List<TeamInvite> teamInvites) {
        return new MyPageDto(userDto, teams, activities, teamInvites);
    }
}
