package wannagohome.domain;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserIncludedInTeam;

public class TeamTest {
    private static final Logger log = LoggerFactory.getLogger(TeamTest.class);

    private Team team;
    private User user;
    private UserIncludedInTeam userIncludedInTeam;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() throws Exception {
        team = Team.builder()
                .name("teamname")
                .description("teamDescription")
                .profileImage("http://urlsurllw")
                .build();

        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password(passwordEncoder.encode("password1"))
                .build();

    }


}
