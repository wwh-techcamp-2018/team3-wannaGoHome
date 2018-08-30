package wannagohome.domain;

import org.junit.Before;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserIncludedInTeam;

public class TeamTest {

    private Team team;
    private User user;
    private UserIncludedInTeam userIncludedInTeam;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() throws Exception {
        team = Team.builder()
                .name("teamname")
                .description("teamDescription")
                .profile("http://urlsurllw")
                .build();

        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password(passwordEncoder.encode("password1"))
                .build();

    }


}
