package wannagohome.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardSummaryDto {

    private List<Board> recentlyViewBoards = new ArrayList<>();
    private List<BoardOfTeamDto> boardOfTeamDtos = new ArrayList<>();

    public void addBoardOfTeamsDTO(BoardOfTeamDto boardOfTeamDTO) {
        boardOfTeamDtos.add(boardOfTeamDTO);
    }

    public void addRecentlyViewBoard(List<Board> recentlyViewBoards) {
        this.recentlyViewBoards.addAll(recentlyViewBoards);
    }


}