package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.Team;
import wannagohome.domain.User;
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
    public Team create(User user, @RequestBody @Valid Team team) {
        return teamService.create(team, user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Team readTeam(@PathVariable Long id) {
        return teamService.findTeamById(id);
    }

//    @GetMapping("")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Team> readTeams(User user) {
//        return teamService.findTeamsByUser(user);
//    }

    //유저없어서 임시로 테스트하려고
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Team> readTeams() {
        return teamService.findAll();
    }



}
