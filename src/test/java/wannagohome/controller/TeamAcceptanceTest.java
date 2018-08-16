package wannagohome.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wannagohome.support.AcceptanceTest;

public class TeamAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(TeamAcceptanceTest.class);

    private static final String CREATE_URL = "/api/teams";
    private static final String READ_URL = "/api/teams/{id}";


    @Test
    public void create() {
        //TODO User 문제해결....하자..
//        Team team = Team.builder()
//                .name("teamname")
//                .description("teamDescription")
//                .profileImage("http://urlsurllw")
//                .build();
//
//        ResponseEntity<Team> responseEntity = template().postForEntity(CREATE_URL, team, Team.class);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        Team responseTeam = responseEntity.getBody();
//        log.debug("description: {}", responseTeam.getDescription());
//        log.debug("name: {}", responseTeam.getName());
//
//        assertThat(team.getName()).isEqualTo(responseTeam.getName());
//        assertThat(team.getDescription()).isEqualTo(responseTeam.getDescription());


    }


}
