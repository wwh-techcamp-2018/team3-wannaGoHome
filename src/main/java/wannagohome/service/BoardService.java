package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.BoardActivity;
import wannagohome.domain.activity.TaskActivity;
import wannagohome.domain.board.*;
import wannagohome.domain.card.Card;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.task.Task;
import wannagohome.domain.task.TaskOrderDto;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserIncludedInBoard;
import wannagohome.domain.user.UserIncludedInTeam;
import wannagohome.domain.user.UserPermission;
import wannagohome.event.BoardEvent;
import wannagohome.event.TeamEvent;
import wannagohome.exception.BadRequestException;
import wannagohome.exception.NotFoundException;
import wannagohome.exception.UnAuthorizedException;
import wannagohome.repository.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Cacheable(value = "boardSummary",key= "#user.id")
    public BoardSummaryDto getBoardSummary(User user) {
        BoardSummaryDto boardSummaryDTO = new BoardSummaryDto();
        boardSummaryDTO.addRecentlyViewBoard(getRecentlyViewBoard(user).stream()
                .map(BoardCardDto::valueOf).collect(Collectors.toList()));
        userIncludedInTeamRepository.findAllByUserAndTeamDeletedFalse(user)
                .forEach(userIncludedInTeam ->
                    boardSummaryDTO.addBoardOfTeamsDTO(
                                new BoardOfTeamDto(
                                        userIncludedInTeam.getTeam(),
                                        getBoardByTeam(userIncludedInTeam.getTeam()).stream().map(BoardCardDto::valueOf).collect(Collectors.toList())
                                )
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
                .findByIdAndDeletedFalse(boardId)
                .orElseThrow(()-> new BadRequestException(ErrorType.BOARD_ID, "ID에 해당하는 Board가 존재하지 않습니다."));
    }

    public BoardInitDto getBoardInitInfo(User user, Long boardId) {
        Board board = findById(boardId);
        List<User> members = userIncludedInBoardRepository.findAllByBoard(board).stream()
                .map(UserIncludedInBoard::getUser).collect(Collectors.toList());
        UserPermission permission = userIncludedInBoardRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "보드에 속해있지 않습니다."))
                .getPermission();
        return BoardInitDto.valueOf(board, members, permission);
    }

    @Transactional
    public BoardHeaderDto deleteBoard(User user, Long boardId) {
        Board board = findById(boardId);
        UserIncludedInBoard userIncludedInBoard = userIncludedInBoardRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> new NotFoundException(ErrorType.BOARD_ID, "유저가 해당 보드에 속해있지 않습니다."));
        board.delete(userIncludedInBoard);
        board = boardRepository.save(board);

        recentlyViewBoardRepository.deleteByBoard(board);

        BoardHeaderDto boardHeaderDto = BoardHeaderDto.valueOf(board, userIncludedInBoard);
        simpMessageSendingOperations.convertAndSend(String.format(Board.BOARD_HEADER_TOPIC_URL, board.getId()), boardHeaderDto);
        return boardHeaderDto;
    }

    public BoardHeaderDto renameBoard(User user, Long boardId, BoardHeaderDto dto) {
        Board board = findById(boardId);
        UserIncludedInBoard userIncludedInBoard = userIncludedInBoardRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> new NotFoundException(ErrorType.BOARD_ID, "유저가 해당 보드에 속해있지 않습니다."));
        board.rename(userIncludedInBoard, dto);
        boardRepository.save(board);

        BoardHeaderDto boardHeaderDto = BoardHeaderDto.valueOf(board, userIncludedInBoard);
        simpMessageSendingOperations.convertAndSend(String.format(Board.BOARD_HEADER_TOPIC_URL, board.getId()), boardHeaderDto);
        return boardHeaderDto;
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

        UserIncludedInBoard userIncludedInBoard = userIncludedInBoardRepository.save(
                UserIncludedInBoard.builder()
                        .user(user)
                        .board(board)
                        .permission(permission)
                        .build()
        );

        BoardActivity boardActivity = BoardActivity.valueOf(user, board, ActivityType.BOARD_MEMBER_ADD);
        applicationEventPublisher.publishEvent(new BoardEvent(this, boardActivity));
        BoardHeaderDto boardHeaderDto = BoardHeaderDto.valueOf(
                board,
                userIncludedInBoardRepository.findAllByBoard(board).stream().map(UserIncludedInBoard::getUser).collect(Collectors.toList()),
                userIncludedInBoard
        );
        simpMessageSendingOperations.convertAndSend(String.format(Board.BOARD_HEADER_TOPIC_URL, board.getId()), boardHeaderDto);

        return userIncludedInBoard;
    }

    @Cacheable(value = "createBoardInfo")
    public CreateBoardInfoDto getCreateBoardInfo(User user) {
        return CreateBoardInfoDto.builder()
                .colors(Arrays.asList(Color.values()))
                .teams(teamService.findTeamsByUser(user))
                .build();
    }

    public List<Card> findCardsByDueDate(Long boardId) {
        Board board = findById(boardId);
        List<Card> cardList = new ArrayList<>();
        board.getTasks().forEach(task -> {
            cardList.addAll(task.getCards().stream().filter(Card::existDueDate).collect(Collectors.toList()));
        });
        return cardList;

    }
}
