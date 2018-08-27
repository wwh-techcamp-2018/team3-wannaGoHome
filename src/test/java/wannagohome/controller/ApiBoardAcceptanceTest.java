package wannagohome.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wannagohome.domain.*;
import wannagohome.repository.BoardRepository;
import wannagohome.repository.RecentlyViewBoardRepository;
import wannagohome.repository.UserIncludedInBoardRepository;
import wannagohome.support.AcceptanceTest;
import wannagohome.support.RequestEntity;

import static org.assertj.core.api.Assertions.assertThat;
public class ApiBoardAcceptanceTest extends AcceptanceTest {


    private final String BASE_BOARD_URL = "/api/boards";
    private SignInDto signInDto;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;


    @Autowired
    private RecentlyViewBoardRepository recentlyViewBoardRepository;
    @Before
    public void setUp() throws Exception {
        signInDto = SignInDto.builder()
                .email("songintae@woowahan.com")
                .password("password1")
                .build();
    }

    @Test
    public void boardSummary() {
        RequestEntity requestEntity = new RequestEntity.Builder()
                .withUrl(BASE_BOARD_URL)
                .withMethod(HttpMethod.GET)
                .withReturnType(BoardSummaryDto.class)
                .build();

        ResponseEntity<BoardSummaryDto> responseEntity = basicAuthRequest(requestEntity, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo((HttpStatus.OK));
        assertThat(responseEntity.getBody().getRecentlyViewBoards().size()).isEqualTo(0);
        assertThat(responseEntity.getBody().getBoardOfTeamDtos().get(0).getTeam().getId()).isEqualTo(1L);
        assertThat(responseEntity.getBody().getBoardOfTeamDtos().get(0).getBoards().size()).isEqualTo(1);
    }


    @Test
    public void createBoardInfo() {
        RequestEntity requestEntity = new RequestEntity.Builder()
                .withUrl(BASE_BOARD_URL + "/createBoardInfo")
                .withMethod(HttpMethod.GET)
                .withReturnType(CreateBoardInfoDto.class)
                .build();

        ResponseEntity<CreateBoardInfoDto> responseEntity = basicAuthRequest(requestEntity, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getColors().size()).isEqualTo(9);
        assertThat(responseEntity.getBody().getTeams().size()).isEqualTo(1);
    }

    @Test
    public void createBoard() {
        CreateBoardDto createBoardDto = CreateBoardDto.builder()
                .teamId(1L)
                .title("woowahan Board")
                .color(Color.DARK_LIME_GREEN.getCode())
                .build();

        RequestEntity requestEntity = new RequestEntity.Builder()
                .withUrl(BASE_BOARD_URL)
                .withMethod(HttpMethod.POST)
                .withBody(createBoardDto)
                .withReturnType(Board.class)
                .build();

        ResponseEntity<Board> responseEntity = basicAuthRequest(requestEntity, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getColor()).isEqualTo(Color.DARK_LIME_GREEN);
        assertThat(responseEntity.getBody().getTitle()).isEqualTo("woowahan Board");
    }
}