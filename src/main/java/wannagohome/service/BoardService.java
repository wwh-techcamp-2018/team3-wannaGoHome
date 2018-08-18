package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import wannagohome.domain.CreateBoardInfoDto;
import wannagohome.domain.*;
import wannagohome.repository.BoardRepository;
import wannagohome.repository.RecentlyViewBoardRepository;
import wannagohome.repository.UserIncludedInBoardRepository;
import wannagohome.repository.UserIncludedInTeamRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private RecentlyViewBoardRepository recentlyViewBoardRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private TeamService teamService;

    @Cacheable(value = "boardSummary",key= "#user.id")
    public BoardSummaryDto getBoardSummary(User user) {
        BoardSummaryDto boardSummaryDTO = new BoardSummaryDto();
        boardSummaryDTO.addRecentlyViewBoard(getRecentlyViewBoard(user));
        userIncludedInTeamRepository.findAllByUser(user)
                .stream()
                .forEach(userIncludedInTeam ->
                    boardSummaryDTO.addBoardOfTeamsDTO(
                                new BoardOfTeamDto(userIncludedInTeam.getTeam(),
                                        getBoardByTeam(userIncludedInTeam.getTeam()))
                    )
                );
        return boardSummaryDTO;
    }
    @Caching(
            evict = {
                    @CacheEvict(value = "recentlyViewBoard", key = "#user.id"),
                    @CacheEvict(value = "boardSummary", key = "#user.id"),
            }
    )
    public Board viewBoard(Long boardId, User user) {
        Board board = findById(boardId);
        recentlyViewBoardRepository.save(
                RecentlyViewBoard.builder()
                        .board(board)
                        .user(user)
                        .build()
        );
        return board;
    }

    @Cacheable(value = "recentlyViewBoard",key= "#user.id")
    public List<Board> getRecentlyViewBoard(User user) {
        return recentlyViewBoardRepository
                .findFirst4ByUserOrderByIdDesc(user)
                .stream()
                .map(recentlyViewBoard ->recentlyViewBoard.getBoard()).collect(Collectors.toList());
    }

    public Board findById(Long boardId) {
        return boardRepository
                .findById(boardId)
                .orElseThrow(RuntimeException::new);
    }

    @Cacheable(value = "boardByTeam",key= "#team.id")
    public List<Board> getBoardByTeam(Team team) {
        return boardRepository.findAllByTeamAndDeletedFalse(team);
    }


    @Caching(
            evict = {
                    @CacheEvict(value = "recentlyViewBoard", key = "#user.id"),
                    @CacheEvict(value = "boardSummary", key = "#user.id"),
            }
    )
    @Transactional
    public Board createBoard(User user, CreateBoardDto createBoardDTO) {
        Board board = Board.builder()
                .team(teamService.findTeamById(createBoardDTO.getTeamId()))
                .title(createBoardDTO.getTitle())
                .color(Color.of(createBoardDTO.getColor()))
                .build();
        board = boardRepository.save(board);
        saveUserIncludedInBoard(user, board, UserPermission.ADMIN);
        return board;
    }

    public UserIncludedInBoard saveUserIncludedInBoard(User user, Board board, UserPermission permission) {
        return userIncludedInBoardRepository.save(
                UserIncludedInBoard.builder()
                .user(user)
                .board(board)
                .permission(permission)
                .build()
        );
    }

    @Cacheable(value = "createBoardInfo")
    public CreateBoardInfoDto getCreateBoardInfo(User user) {
        return CreateBoardInfoDto.builder()
                .colors(Arrays.asList(Color.values()))
                .teams(teamService.findTeamsByUser(user))
                .build();
    }
}
