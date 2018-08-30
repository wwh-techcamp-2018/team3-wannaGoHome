package wannagohome.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserIncludedInTeam;
import wannagohome.domain.user.UserPermission;
import wannagohome.exception.DuplicationException;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.TeamRepository;
import wannagohome.repository.UserIncludedInTeamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceMockTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @InjectMocks
    private TeamService teamService;

    private Team team;
    private Team team2;
    private List<UserIncludedInTeam> userIncludedInTeams = new ArrayList<>();
    private User user;
    private User user2;
    private UserIncludedInTeam userIncludedInTeam;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() throws Exception {
        team = Team.builder()
                .name("teamname")
                .description("teamDescription")
                .profile("http://urlsurllw")
                .build();

        team2 = Team.builder()
                .name("teamname2")
                .description("teamDescription2")
                .profile("http://urlsurllw2")
                .build();

        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password(passwordEncoder.encode("password1"))
                .build();

        user2 = User.builder()
                .email("jhyang@good.looking2")
                .name("jhyang2")
                .password(passwordEncoder.encode("password12"))
                .build();

        userIncludedInTeams.add(new UserIncludedInTeam(user, team, UserPermission.ADMIN));
        userIncludedInTeams.add(new UserIncludedInTeam(user, team2, UserPermission.ADMIN));
        when(teamRepository.save(team)).thenReturn(team);
    }

    @Test
    public void create() {
        teamService.create(team, user);
        verify(teamRepository, times(1)).save(any());
        verify(userIncludedInTeamRepository, times(1)).save(any());
    }

    @Test
    public void findTeamsByUser() {
        when(userIncludedInTeamRepository.findAllByUserAndTeamDeletedFalse(user)).thenReturn(userIncludedInTeams);
        teamService.findTeamsByUser(user);
        verify(userIncludedInTeamRepository, times(1)).findAllByUserAndTeamDeletedFalse(user);
    }

    @Test(expected = NotFoundException.class)
    public void findTeamsById() {
        teamService.findTeamById(10L);
    }

    @Test(expected = DuplicationException.class)
    public void create_이미존재하는팀이름() {
        when(teamRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.ofNullable(team));
        teamService.create(team, user);
        teamService.create(team, user2);
    }



}
