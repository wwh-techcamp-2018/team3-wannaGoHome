package wannagohome.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.*;
import wannagohome.repository.BoardRepository;
import wannagohome.repository.RecentlyViewBoardRepository;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    @Setter
    private BoardRepository boardRepository;

    @Autowired
    @Setter
    private RecentlyViewBoardRepository recentlyViewBoardRepository;


    public BoardSummaryDTO getBoardSummary(User user) {
        return new BoardSummaryDTO();
    }

    public Board viewBoard(Long boardId, User user) {
        Board board = findById(boardId);
        recentlyViewBoardRepository.save(RecentlyViewBoard.builder()
                .board(board)
                .user(user)
                .build());
        return board;
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(RuntimeException::new);
    }

    public List<Board> getBoardByTeam(Team team) {
        return boardRepository.findAllByTeam(team);
    }
}
