package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamInvite;
import wannagohome.domain.user.User;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.TeamInviteRepository;
import wannagohome.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

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

    public List<TeamInvite> findAllByMember(User member) {
        return teamInviteRepository.findAllByMember(member);
    }

    public void deleteById(Long id) {
        teamInviteRepository.deleteById(id);
    }

    public TeamInvite findById(Long id) {
        return teamInviteRepository.findById(id).orElseThrow(()->new NotFoundException(ErrorType.TEAM_INVITE_ID,"팀 초대를 찾을 수 없습니다."));
    }
}
