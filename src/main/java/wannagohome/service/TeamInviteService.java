package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamInvite;
import wannagohome.domain.user.User;
import wannagohome.repository.TeamInviteRepository;
import wannagohome.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class TeamInviteService {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamInviteRepository teamInviteRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TeamInvite createTeamInvite(Long userId, Long teamId) {
        Team team = teamService.findTeamById(teamId);
        User user = userRepository.findById(userId).get();
        if (teamInviteRepository.findByMemberEqualsAndTeamEquals(user, team).isPresent()) {
            return null;
        } else {
            TeamInvite teamInvite = new TeamInvite(user, team);
            return teamInviteRepository.save(teamInvite);
        }
    }
}
