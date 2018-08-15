package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.Team;
import wannagohome.repository.TeamRepository;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team findById(Long id) {
        return teamRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
