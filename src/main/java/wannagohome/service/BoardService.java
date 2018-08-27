package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import wannagohome.domain.*;
import wannagohome.event.BoardEvent;
import wannagohome.event.TeamEvent;
import wannagohome.exception.BadRequestException;
import wannagohome.exception.NotFoundException;
import wannagohome.exception.UnAuthorizedException;
import wannagohome.repository.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private RecentlyViewBoardRepository recentlyViewBoardRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Cacheable(value = "boardSummary",key= "#user.id")
    public BoardSummaryDto getBoardSummary(User user) {
        BoardSummaryDto boardSummaryDTO = new BoardSummaryDto();
        boardSummaryDTO.addRecentlyViewBoard(getRecentlyViewBoard(user).stream()
                .map(board -> BoardCardDto.valueOf(board)).collect(Collectors.toList()));
        userIncludedInTeamRepository.findAllByUser(user)
                .forEach(userIncludedInTeam ->
                    boardSummaryDTO.addBoardOfTeamsDTO(
                                new BoardOfTeamDto(userIncludedInTeam.getTeam(),
                                        getBoardByTeam(userIncludedInTeam.getTeam()).stream()
                                                .map(board -> BoardCardDto.valueOf(board)).collect(Collectors.toList()))
                    )
                );
        return boardSummaryDTO;
    }
    @Caching(
            evict = {
                    @CacheEvict(value = "recentlyViewBoard", key = "#user.id"),
                    @CacheEvict(value = "boardSummary", key = "#user.id")
            }
    )
    @Transactional
    public Board viewBoard(Long boardId, User user) {
        Board board = findById(boardId);
        confirmAuthorityOfUser(user, board);
        saveRecentlyViewBoard(user, board);
        saveUserIncludedInBoard(user, board, UserPermission.MEMBER);
        return board;
    }

    private UserIncludedInTeam confirmAuthorityOfUser(User user, Board board) {
        return userIncludedInTeamRepository.findByUserAndTeam(user,board.getTeam())
                .orElseThrow(() -> new UnAuthorizedException(ErrorType.UNAUTHORIZED, "Board에 접근할 권한이 없습니다."));

    }
    private RecentlyViewBoard saveRecentlyViewBoard(User user, Board board) {
        return recentlyViewBoardRepository.save(
                RecentlyViewBoard.builder()
                        .board(board)
                        .user(user)
                        .build()
        );
    }

    @Transactional
    public Board addBoardTask(Long boardId, Task newTask) {
        Board board = findById(boardId);
        newTask.setBoard(board);
        board.addTask(newTask);
        TaskActivity taskActivity = TaskActivity.valueOf(newTask.getAuthor(), newTask, ActivityType.TASK_CREATE);
        applicationEventPublisher.publishEvent(new BoardEvent(this, taskActivity));
        return boardRepository.save(board);
    }

    @Transactional
    public Board reorderBoardTasks(Long boardId, TaskOrderDto taskOrderDto) {
        Board board = findById(boardId);
        board.reorderTasks(taskOrderDto);
        return boardRepository.save(board);
    }

    @Cacheable(value = "recentlyViewBoard",key= "#user.id")
    public List<Board> getRecentlyViewBoard(User user) {
        return recentlyViewBoardRepository
                .findFirst4ByUserOrderByIdDesc(user.getId())
                .stream()
                .map(RecentlyViewBoard::getBoard).collect(Collectors.toList());
    }

    public Board findById(Long boardId) {
        return boardRepository
                .findById(boardId)
                .orElseThrow(()-> new BadRequestException(ErrorType.BOARD_ID, "ID에 해당하는 Board가 존재하지 않습니다."));
    }

    @Cacheable(value = "boardByTeam",key= "#team.id")
    public List<Board> getBoardByTeam(Team team) {
        return boardRepository.findAllByTeamAndDeletedFalse(team);
    }


    @Caching(
            evict = {
                    @CacheEvict(value = "recentlyViewBoard", key = "#user.id"),
                    @CacheEvict(value = "boardSummary", allEntries = true),
            }
    )
    @Transactional
    public Board createBoard(User user, CreateBoardDto createBoardDTO) {
        Team team = teamService.findTeamById(createBoardDTO.getTeamId());
        Board board = Board.builder()
                .team(team)
                .title(createBoardDTO.getTitle())
                .color(Color.of(createBoardDTO.getColor()))
                .build();
        board = boardRepository.save(board);
        saveUserIncludedInBoard(user, board, UserPermission.ADMIN);
        BoardActivity boardActivity = BoardActivity.valueOf(user, board, ActivityType.BOARD_CREATE);
        applicationEventPublisher.publishEvent(new TeamEvent(this, boardActivity));
        return board;
    }

    public UserIncludedInBoard saveUserIncludedInBoard(User user, Board board, UserPermission permission) {
        Optional<UserIncludedInBoard> maybeUserIncludedInBoard =
                userIncludedInBoardRepository.findByUserAndBoard(user,board);
        if(maybeUserIncludedInBoard.isPresent()) {
            return maybeUserIncludedInBoard.get();
        }

        BoardActivity boardActivity = BoardActivity.valueOf(user, board, ActivityType.BOARD_MEMBER_ADD);
        applicationEventPublisher.publishEvent(new BoardEvent(this, boardActivity));
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

    public List<Card> findCardsByDueDate(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorType.BOARD_ID, "일치하는 보드가 없습니다."));
        List<Card> cardList = new ArrayList<>();
        board.getTasks().forEach(task -> {
            cardList.addAll(task.getCards().stream().filter(card -> card.existDueDate()).collect(Collectors.toList()));
        });
        return cardList;

    }
}
