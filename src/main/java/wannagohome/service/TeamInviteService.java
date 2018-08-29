package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.TeamActivity;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamInvite;
import wannagohome.domain.user.User;
import wannagohome.event.ActivityEventHandler;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.ActivityRepository;
import wannagohome.repository.TeamInviteRepository;
import wannagohome.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
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

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityEventHandler activityEventHandler;

    @Transactional
    public TeamInvite createTeamInvite(User invitor, Long userId, Long teamId) {
        Team team = teamService.findTeamById(teamId);
        User user = userRepository.findById(userId).get();
        if (teamInviteRepository.findByMemberEqualsAndTeamEquals(user, team).isPresent()) {
            //client에서 처리.
            return null;
        } else {
            notifyInvitation(invitor, user, team);
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

    private void notifyInvitation(User invitor, User receiver, Team team) {
        TeamActivity activity = TeamActivity.valueOf(invitor, team, ActivityType.TEAM_MEMBER_INVITE);
        activity.setReceiver(receiver);
        activityRepository.save(activity);
        activityEventHandler.sendPersonalMessage(receiver, Arrays.asList(activity));
    }
}
