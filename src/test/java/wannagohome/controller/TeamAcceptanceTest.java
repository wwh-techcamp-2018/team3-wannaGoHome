package wannagohome.controller;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wannagohome.domain.BoardOfTeamDto;
import wannagohome.domain.SignInDto;
import wannagohome.domain.Team;
import wannagohome.repository.TeamRepository;
import wannagohome.support.AcceptanceTest;
import wannagohome.support.RequestEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(TeamAcceptanceTest.class);

    private static final String CREATE_URL = "/api/teams";
    private static final String READ_URL = "/api/teams";

    @Autowired
    private TeamRepository teamRepository;

    private Team team;
    private Team team2;

    @Before
    public void setUp() throws Exception {
        team = Team.builder()
                .name("test")
                .description("testDescription")
                .profileImage("http://urlsurllw")
                .build();
        team2 = Team.builder()
                .name("test2")
                .description("testDescription2")
                .profileImage("http://urlsurllw2")
                .build();
    }

    @Test
    public void createAndReadTeam() {
        ResponseEntity<BoardOfTeamDto> createResponseEntity = basicAuthRequest(new RequestEntity.Builder()
                        .withUrl(CREATE_URL)
                        .withBody(team)
                        .withMethod(HttpMethod.POST)
                        .withReturnType(BoardOfTeamDto.class)
                        .build(),
                new SignInDto("kimyeon@woowahan.com", "password1")
        );
        Team responseTeam = createResponseEntity.getBody().getTeam();
        log.debug("description: {}", responseTeam.getDescription());
        log.debug("name: {}", responseTeam.getName());
        assertThat(createResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(team.getName()).isEqualTo(responseTeam.getName());
        assertThat(team.getDescription()).isEqualTo(responseTeam.getDescription());


        ResponseEntity<BoardOfTeamDto> createResponseEntity2 = basicAuthRequest(new RequestEntity.Builder()
                        .withUrl(CREATE_URL)
                        .withBody(team2)
                        .withMethod(HttpMethod.POST)
                        .withReturnType(BoardOfTeamDto.class)
                        .build(),
                new SignInDto("kimyeon@woowahan.com", "password1")
        );
        assertThat(createResponseEntity2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Team responseTeam2 = createResponseEntity2.getBody().getTeam();
        log.debug("description: {}", responseTeam2.getDescription());
        log.debug("name: {}", responseTeam2.getName());
        assertThat(createResponseEntity2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(team2.getName()).isEqualTo(responseTeam2.getName());
        assertThat(team2.getDescription()).isEqualTo(responseTeam2.getDescription());


        ResponseEntity<Team[]> responseEntity  = basicAuthRequest(new RequestEntity.Builder()
                        .withUrl(READ_URL)
                        .withMethod(HttpMethod.GET)
                        .withReturnType(Team[].class)
                        .build(),
                new SignInDto("kimyeon@woowahan.com", "password1")
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Team> responseTeams = Arrays.asList(responseEntity.getBody());
        assertThat(responseTeams.stream().map(Team::getName)).contains("WannaGoHome", "test", "test2");
    }


    @Test
    public void readTeam() {
        ResponseEntity<Team> readResponseEntity = basicAuthRequest(
                new RequestEntity.Builder()
                .withMethod(HttpMethod.GET)
                .withUrl(READ_URL + "/1")
                .withReturnType(Team.class)
                .build(),
                new SignInDto("junsulime@woowahan.com", "password1")
        );
        assertThat(readResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Team responseTeam = readResponseEntity.getBody();
        log.debug("description: {}", responseTeam.getDescription());
        log.debug("name: {}", responseTeam.getName());
        assertThat(responseTeam.getName()).isEqualTo("WannaGoHome");
    }

}
