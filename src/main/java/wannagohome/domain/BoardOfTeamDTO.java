package wannagohome.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardOfTeamDTO {
    private Team team;
    private List<Board> boards = new ArrayList<>();
}
