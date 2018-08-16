package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import wannagohome.domain.*;
import wannagohome.repository.TeamRepository;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.UserIncludedInTeamRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Transactional
    public Team create(Team team, User user) {
        Team newTeam = teamRepository.save(team);
        userIncludedInTeamRepository.save(createRelation(user, newTeam, UserPermission.ADMIN));
        return newTeam;
    }

    public UserIncludedInTeam createRelation(User user, Team team, UserPermission userPermission) {
       return new UserIncludedInTeam(user, team, userPermission);
    }

    @Cacheable(value = "teamById",key= "#id")
    public Team findTeamById(Long id) {
        return teamRepository.findByIdAndDeletedFalse(id)
        .orElseThrow(() -> new NotFoundException(ErrorType.TEAM_NAME, "Team이 존재하지 않습니다."));
    }

    @Cacheable(value = "teamsByUser", key="#user.id")
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
