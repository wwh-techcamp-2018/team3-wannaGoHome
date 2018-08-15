package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.*;
import wannagohome.repository.BoardRepository;
import wannagohome.repository.RecentlyViewBoardRepository;
import wannagohome.repository.UserIncludedInBoardRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private RecentlyViewBoardRepository recentlyViewBoardRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private TeamService teamService;


    public BoardSummaryDTO getBoardSummary(User user) {
        BoardSummaryDTO boardSummaryDTO = new BoardSummaryDTO();
        boardSummaryDTO.addRecentlyViewBoard(getRecentlyViewBoard(user));
        user.getIncludedTeams()
                .stream()
                .forEach(userIncludedInTeam ->
                    boardSummaryDTO.addBoardOfTeamsDTO(
                                new BoardOfTeamDTO(userIncludedInTeam.getTeam(),
                                        getBoardByTeam(userIncludedInTeam.getTeam()))
                    )
                );
        return boardSummaryDTO;
    }

    public Board viewBoard(Long boardId, User user) {
        Board board = findById(boardId);
        recentlyViewBoardRepository.save(RecentlyViewBoard.builder()
                .board(board)
                .user(user)
                .build());
        return board;
    }

    public List<Board> getRecentlyViewBoard(User user) {
        List<Board> recentlyViewBoards = new ArrayList<>();
        recentlyViewBoardRepository
                .findFirst3ByUserOrderByIdDesc(user)
                .stream()
                .forEach(recentlyViewBoard -> recentlyViewBoards.add(recentlyViewBoard.getBoard()));
        return recentlyViewBoards;
    }

    public Board findById(Long boardId) {
        return boardRepository
                .findById(boardId)
                .orElseThrow(RuntimeException::new);
    }

    public List<Board> getBoardByTeam(Team team) {
        return boardRepository.findAllByTeam(team);
    }

    public Board createBoard(User user, CreateBoardDTO createBoardDTO) {
        Board board = Board.builder()
                .team(teamService.findById(createBoardDTO.getTeamId()))
                .title(createBoardDTO.getTitle())
                .color(Color.of(createBoardDTO.getColor()))
                .build();
        saveUserIncludedInBoard(user, board, UserPermission.ADMIN);
        return boardRepository.save(board);
    }

    public UserIncludedInBoard saveUserIncludedInBoard(User user, Board board, UserPermission permission) {
        return userIncludedInBoardRepository.save(UserIncludedInBoard.builder()
                .user(user)
                .board(board)
                .permission(permission)
                .build());
    }
}
