package wannagohome.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import wannagohome.domain.*;
import wannagohome.repository.BoardRepository;
import wannagohome.repository.RecentlyViewBoardRepository;
import wannagohome.repository.UserIncludedInBoardRepository;
import wannagohome.repository.UserIncludedInTeamRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private RecentlyViewBoardRepository recentlyViewBoardRepository;
    @Mock
    private UserIncludedInBoardRepository userIncludedInBoardRepository;
    @Mock
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private TeamService teamService;

    @InjectMocks
    private BoardService boardService;


    private User user;
    private Team team;
    private List<Board> boards;
    private List<RecentlyViewBoard> recentlyViewBoards;

    @Before
    public void setUp() throws Exception {
        team = Team.builder()
                .id(1L)
                .name("wannagohome")
                .description("wannagohome 화이팅")
                .profileImage("default.png")
                .build();

        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password("password1")
                .build();

        boards = Arrays.asList(
                Board.builder().id(1L).title("one board").team(team).build(),
                Board.builder().id(2L).title("two board").team(team).build(),
                Board.builder().id(3L).title("three board").team(team).build(),
                Board.builder().id(4L).title("four board").team(team).build()
        );

        recentlyViewBoards = Arrays.asList(
                RecentlyViewBoard.builder().board(boards.get(0)).build(),
                RecentlyViewBoard.builder().board(boards.get(1)).build(),
                RecentlyViewBoard.builder().board(boards.get(2)).build(),
                RecentlyViewBoard.builder().board(boards.get(3)).build()
        );

    }


    @Test
    public void getBoardByTeam_조회() {
        when(boardRepository.findAllByTeamAndDeletedFalse(team)).thenReturn(boards);
        assertThat(boardService.getBoardByTeam(team).size()).isEqualTo(4);
        verify(boardRepository,times(1)).findAllByTeamAndDeletedFalse(team);
    }

    @Test
    public void viewBoard_요청시_RecentlyViewBoard_등록(){
        when(recentlyViewBoardRepository.save(any())).thenReturn(null);
        when(boardRepository.findById(boards.get(0).getId())).thenReturn(Optional.ofNullable(boards.get(0)));
        when(userIncludedInTeamRepository.findByUserAndTeam(user,boards.get(0).getTeam()))
                .thenReturn(Optional.ofNullable(UserIncludedInTeam.builder().id(1L).team(team).user(user).build()));
        assertThat(boardService.viewBoard(boards.get(0).getId(), user)).isEqualTo(boards.get(0));
        verify(boardRepository,times(1)).findById(boards.get(0).getId());
        verify(recentlyViewBoardRepository,times(1)).save(any());
    }

    @Test
    public void getRecentlyViewBoard_조회() {
        when(recentlyViewBoardRepository.findFirst4ByUserOrderByIdDesc(user.getId())).thenReturn(recentlyViewBoards);
        List<Board> recentlyViewBoards = boardService.getRecentlyViewBoard(user);
        assertThat(recentlyViewBoards.size()).isEqualTo(4);
        verify(recentlyViewBoardRepository,times(1)).findFirst4ByUserOrderByIdDesc(user.getId());
    }


    @Test
    public void getBoardSummary_조회() {
        UserIncludedInTeam userIncludedInTeam = UserIncludedInTeam.builder().team(team).build();
        when(recentlyViewBoardRepository.findFirst4ByUserOrderByIdDesc(user.getId())).thenReturn(recentlyViewBoards);
        when(boardRepository.findAllByTeamAndDeletedFalse(team)).thenReturn(boards);
        when(userIncludedInTeamRepository.findAllByUser(user)).thenReturn(Arrays.asList(userIncludedInTeam));
        BoardSummaryDto boardSummaryDTO = boardService.getBoardSummary(user);
        assertThat(boardSummaryDTO.getRecentlyViewBoards().size()).isEqualTo(4);
        assertThat(boardSummaryDTO.getBoardOfTeamDtos().size()).isEqualTo(1);
        boardSummaryDTO
                .getBoardOfTeamDtos()
                .stream()
                .forEach(boardOfTeamDTO -> {
                    assertThat(boardOfTeamDTO.getTeam()).isEqualTo(team);
                    assertThat(boardOfTeamDTO.getBoards().size()).isEqualTo(4);
                });
    }

    @Test
    public void createBoard_생성() {
        CreateBoardDto boardDTO = CreateBoardDto.builder()
                .teamId(team.getId())
                .title("create board")
                .color(Color.DARK_LIME_GREEN.getCode())
                .build();
        when(boardRepository.save(any())).thenReturn(boards.get(0));
        when(userIncludedInBoardRepository.save(any())).thenReturn(null);
        when(teamService.findTeamById(team.getId())).thenReturn(team);
        Board createBoard = boardService.createBoard(user,boardDTO);
        assertThat(createBoard).isEqualTo(boards.get(0));
        verify(boardRepository,times(1)).save(any());
        verify(userIncludedInBoardRepository,times(1)).save(any());
        verify(teamService,times(1)).findTeamById(any());
    }

    @Test
    public void saveUserIncludedInBoard_생성() {
        UserIncludedInBoard userIncludedInBoard = UserIncludedInBoard.builder()
                .user(user)
                .board(boards.get(0))
                .permission(UserPermission.ADMIN)
                .build();
        when(userIncludedInBoardRepository.save(any())).thenReturn(userIncludedInBoard);
        assertThat(boardService.saveUserIncludedInBoard(user,boards.get(0),UserPermission.ADMIN))
                .isEqualTo(userIncludedInBoard);
        verify(userIncludedInBoardRepository,times(1)).save(any());
    }

    @Test
    public void getCreateBoardInfo() {
        when(teamService.findTeamsByUser(user)).thenReturn(Arrays.asList(team));
        CreateBoardInfoDto createBoardInfoDto = boardService.getCreateBoardInfo(user);
        assertThat(createBoardInfoDto.getTeams().size()).isEqualTo(1);
        assertThat(createBoardInfoDto.getColors().size()).isEqualTo(9);
    }

}