package wannagohome.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardSummaryDTO {

    private List<Board> recentlyViewBoards = new ArrayList<>();
    private List<BoardOfTeamDTO> boardOfTeamDTOs = new ArrayList<>();

    public void addBoardOfTeamsDTO(BoardOfTeamDTO boardOfTeamDTO) {
        boardOfTeamDTOs.add(boardOfTeamDTO);
    }

    public void addRecentlyViewBoard(List<Board> recentlyViewBoards) {
        this.recentlyViewBoards.addAll(recentlyViewBoards);
    }


}
