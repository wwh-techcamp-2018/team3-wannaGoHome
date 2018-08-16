package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.Team;
import wannagohome.domain.User;
import wannagohome.domain.UserIncludedInTeam;
import wannagohome.domain.UserPermission;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.TeamRepository;
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
        Team newTeam = teamRepository.save(team);
        userIncludedInTeamRepository.save(createRelation(user, newTeam, UserPermission.ADMIN));
        return newTeam;
    }

    public UserIncludedInTeam createRelation(User user, Team team, UserPermission userPermission) {
       return new UserIncludedInTeam(user, team, userPermission);
    }


    public Team findTeamById(Long id) {
        return teamRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Team이 존재하지 않습니다."));
    }

    public List<Team> findTeamsByUser(User user) {
        List<Team> teams = new ArrayList<>();
        userIncludedInTeamRepository.findAllByUser(user)
                .stream()
                .forEach(userIncludedInTeam -> teams.add(userIncludedInTeam.getTeam()));
        return teams;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }
}
