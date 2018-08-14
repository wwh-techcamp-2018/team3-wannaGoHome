package wannagohome.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardSummaryDTO {

    private List<Board> recentlyBoards = new ArrayList<>();
    private List<BoardOfTeamDTO> boardOfTeamDTOs = new ArrayList<>();

}
