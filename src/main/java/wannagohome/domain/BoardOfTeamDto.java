package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardOfTeamDto {
    private Team team;
    private List<Board> boards = new ArrayList<>();
}
