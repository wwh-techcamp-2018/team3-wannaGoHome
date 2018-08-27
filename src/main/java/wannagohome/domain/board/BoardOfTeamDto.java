package wannagohome.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.team.Team;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardOfTeamDto {
    private Team team;
    private List<BoardCardDto> boards = new ArrayList<>();

    public BoardOfTeamDto(Team team) {
        this.team = team;
    }
}
