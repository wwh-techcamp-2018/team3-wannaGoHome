package wannagohome.domain.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.team.Team;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBoardInfoDto {
    public List<Team> teams = new ArrayList<>();
    public List<Color> colors = new ArrayList<>();

}
