package wannagohome.controller.api;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wannagohome.domain.BoardSummaryDto;
import wannagohome.domain.SignInDto;
import wannagohome.support.AcceptanceTest;
import wannagohome.support.RequestEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiBoardControllerTest  extends AcceptanceTest {

    SignInDto signInDto;
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
                .withUrl("/api/boards")
                .withMethod(HttpMethod.GET)
                .withReturnType(BoardSummaryDto.class)
                .build();

        ResponseEntity<BoardSummaryDto> responseEntity = basicAuthRequest(requestEntity, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo((HttpStatus.OK));
        assertThat(responseEntity.getBody().getRecentlyViewBoards().size()).isEqualTo(4);
        assertThat(responseEntity.getBody().getBoardOfTeamDtos().get(0).getTeam().getId()).isEqualTo(1L);
        assertThat(responseEntity.getBody().getBoardOfTeamDtos().get(0).getBoards().size()).isEqualTo(4);
    }
}