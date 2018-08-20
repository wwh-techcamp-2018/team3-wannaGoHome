package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.BoardOfTeamDto;
import wannagohome.domain.Team;
import wannagohome.domain.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.TeamService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class ApiTeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BoardOfTeamDto create(@LoginUser User user, @RequestBody @Valid Team team) {
        return teamService.create(team, user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Team readTeam(@PathVariable Long id) {
        return teamService.findTeamById(id);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Team> readTeams(@LoginUser User user) {
        return teamService.findTeamsByUser(user);
    }


}
