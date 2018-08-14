package wannagohome.service;

import org.junit.Before;
import org.junit.Test;
import wannagohome.domain.Board;
import wannagohome.domain.Team;
import wannagohome.domain.User;
import wannagohome.repository.BoardRepository;
import wannagohome.repository.RecentlyViewBoardRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class BoardServiceTest {

    private BoardService boardService;
    private BoardRepository boardRepository;
    private RecentlyViewBoardRepository recentlyViewBoardRepository;
    private User user;
    private Team team;
    private List<Board> boards;

    @Before
    public void setUp() throws Exception {

        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password("password1")
                .build();

        team = Team.builder()
                .id(1L)
                .name("wannagohome")
                .description("wannagohome 화이팅")
                .profileImage("default.png")
                .build();
        boards = Arrays.asList(
                Board.builder().id(1L).title("one board").team(team).build(),
                Board.builder().id(2L).title("two board").team(team).build(),
                Board.builder().id(3L).title("three board").team(team).build()
        );
        boardService = new BoardService();
        boardRepository = mock(BoardRepository.class);
        recentlyViewBoardRepository = mock(RecentlyViewBoardRepository.class);
        boardService.setBoardRepository(boardRepository);
    }


    @Test
    public void getBoardByTeam_조회() {
        when(boardRepository.findAllByTeam(team)).thenReturn(boards);
        assertThat(boardService.getBoardByTeam(team).size()).isEqualTo(3);
        verify(boardRepository,times(1)).findAllByTeam(team);
    }

    @Test
    public void viewBoard_요청시_RecentlyViewBoard_등록(){
        when(recentlyViewBoardRepository.save(any())).thenReturn(null);
        when(boardRepository.findById(boards.get(0).getId())).thenReturn(Optional.ofNullable(boards.get(0)));
        assertThat(boardService.viewBoard(boards.get(0).getId(), user)).isEqualTo(boards.get(0));
        verify(boardRepository,times(1)).findById(boards.get(0).getId());
        verify(recentlyViewBoardRepository,times(1)).save(any());
    }
}