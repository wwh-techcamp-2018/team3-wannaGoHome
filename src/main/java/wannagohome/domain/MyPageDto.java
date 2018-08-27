package wannagohome.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
public class MyPageDto {
    UserDto user;
    List<Team> teams;
    List<ActivityDto> activities;

    private MyPageDto(UserDto user, List<Team> teams, List<ActivityDto> activities) {
        this.user = user;
        this.teams = teams;
        this.activities = activities;
    }


    public static MyPageDto valueOf(UserDto userDto, List<Team> teams, List<ActivityDto> activities) {
        return new MyPageDto(userDto, teams, activities);
    }
}
