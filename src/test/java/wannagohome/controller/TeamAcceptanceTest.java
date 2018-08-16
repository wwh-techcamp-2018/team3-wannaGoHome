package wannagohome.controller;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wannagohome.domain.SignInDto;
import wannagohome.domain.Team;
import wannagohome.support.AcceptanceTest;
import wannagohome.support.RequestEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TeamAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(TeamAcceptanceTest.class);

    private static final String CREATE_URL = "/api/teams";
    private static final String READ_URL = "/api/teams";


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
    public void create() {
        ResponseEntity<Team> responseEntity = basicAuthRequest(new RequestEntity.Builder()
                .withUrl(CREATE_URL)
                .withBody(team)
                .withMethod(HttpMethod.POST)
                .withReturnType(Team.class)
                .build(),
                new SignInDto("kimyeon@woowahan.com", "password1")
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Team responseTeam = responseEntity.getBody();
        log.debug("description: {}", responseTeam.getDescription());
        log.debug("name: {}", responseTeam.getName());

        assertThat(team.getName()).isEqualTo(responseTeam.getName());
        assertThat(team.getDescription()).isEqualTo(responseTeam.getDescription());
    }

    @Test
    public void readTeam() {
        ResponseEntity<Team> responseEntity = template().getForEntity(READ_URL+"/1", Team.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Team responseTeam = responseEntity.getBody();
        log.debug("description: {}", responseTeam.getDescription());
        log.debug("name: {}", responseTeam.getName());
        assertThat(responseTeam.getName()).isEqualTo("JunsuLime");
    }

    @Test
    public void readTeamsAfterCreate() {
        ResponseEntity<Team> createResponseEntity = basicAuthRequest(new RequestEntity.Builder()
                        .withUrl(CREATE_URL)
                        .withBody(team)
                        .withMethod(HttpMethod.POST)
                        .withReturnType(Team.class)
                        .build(),
                new SignInDto("kimyeon@woowahan.com", "password1")
        );
        assertThat(createResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Team> createResponseEntity2 = basicAuthRequest(new RequestEntity.Builder()
                        .withUrl(CREATE_URL)
                        .withBody(team2)
                        .withMethod(HttpMethod.POST)
                        .withReturnType(Team.class)
                        .build(),
                new SignInDto("kimyeon@woowahan.com", "password1")
        );
        assertThat(createResponseEntity2.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Team[]> responseEntity  = basicAuthRequest(new RequestEntity.Builder()
                        .withUrl(READ_URL)
                        .withMethod(HttpMethod.GET)
                        .withReturnType(Team[].class)
                        .build(),
                new SignInDto("kimyeon@woowahan.com", "password1")
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Team> responseTeams = Arrays.asList(responseEntity.getBody());
        for (Team responseTeam : responseTeams) {
            log.debug("description: {}", responseTeam.getDescription());
            log.debug("name: {}", responseTeam.getName());
            assertThat(responseTeam.getName()).contains("test");
        }
    }

    @Test
    public void readTeamAfterCreate() {
        ResponseEntity<Team> createResponseEntity = basicAuthRequest(new RequestEntity.Builder()
                        .withUrl(CREATE_URL)
                        .withBody(team)
                        .withMethod(HttpMethod.POST)
                        .withReturnType(Team.class)
                        .build(),
                new SignInDto("kimyeon@woowahan.com", "password1")
        );
        assertThat(createResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Team createResponseTeam = createResponseEntity.getBody();

        ResponseEntity<Team> readResponseEntity = template().getForEntity(READ_URL+"/4", Team.class);
        assertThat(readResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Team readResponseTeam = readResponseEntity.getBody();

        assertThat(createResponseTeam.getName()).isEqualTo(readResponseTeam.getName());
        assertThat(createResponseTeam.getDescription()).isEqualTo(readResponseTeam.getDescription());

    }
}
