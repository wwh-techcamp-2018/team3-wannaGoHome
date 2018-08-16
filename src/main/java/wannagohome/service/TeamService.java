package wannagohome.service;

import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.Team;
import wannagohome.domain.User;
import wannagohome.domain.UserIncludedInTeam;
import wannagohome.repository.TeamRepository;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.UserIncludedInTeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    public Team create(Team team, User user) {
        //TODO
        userIncludedInTeamRepository.save(team.createRelation(user, team));
        return teamRepository.save(team);
    }


    public Team findTeamById(Long id) {
        return teamRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Team이 존재하지 않습니다."));
    }

    public List<Team> findTeamsByUser(User user) {
        List<Team> teams = new ArrayList<>();
        List<UserIncludedInTeam> userIncludedInTeams = userIncludedInTeamRepository.findAllByUser(user);
        //TODO 멘붕... 유저와 팀을 이어주는 테이블을 가져왔는데 거기서 이제 팀들을 뽑아서 팀리스트를 만들어야함
        //TODO Map 사용
        for (UserIncludedInTeam userIncludedInTeam : userIncludedInTeams) {
            teams.add(userIncludedInTeam.getTeam());
        }
        return teams;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }
}
