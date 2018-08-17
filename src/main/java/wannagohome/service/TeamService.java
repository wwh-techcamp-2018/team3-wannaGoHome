package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import wannagohome.domain.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.TeamRepository;
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
    @Caching(
            evict = {
                    @CacheEvict(value = "boardSummary",key= "#user.id"),
                    @CacheEvict(value = "recentlyViewBoard",key= "#user.id"),
                    @CacheEvict(value = "teamsByUser",key= "#user.id")
            }
    )
    public Team create(Team team, User user) {
        Team newTeam = teamRepository.save(team);
        userIncludedInTeamRepository.save(createRelation(user, newTeam, UserPermission.ADMIN));
        return newTeam;
    }

    private UserIncludedInTeam createRelation(User user, Team team, UserPermission userPermission) {
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
