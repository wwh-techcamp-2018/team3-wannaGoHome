package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.board.BoardOfTeamDto;
import wannagohome.domain.error.ErrorEntity;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamInvite;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserDto;
import wannagohome.event.ActivityEventHandler;
import wannagohome.exception.DuplicationException;
import wannagohome.exception.ErrorEntityException;
import wannagohome.interceptor.LoginUser;
import wannagohome.repository.ActivityRepository;
import wannagohome.service.TeamInviteService;
import wannagohome.service.TeamService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class ApiTeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamInviteService teamInviteService;

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

    @GetMapping("/{id}/members")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> teamMembers(@PathVariable Long id) {
        return teamService.findByTeam(teamService.findTeamById(id));
    }

    @GetMapping("/{id}/member")
    @ResponseStatus(HttpStatus.OK)
    public UserDto teamMember(@LoginUser User user, @PathVariable Long id) {
        return teamService.findByUserOfTeam(user,teamService.findTeamById(id));
    }

    @GetMapping("/{id}/search/{queryString}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> searchResults(@PathVariable Long id, @PathVariable String queryString) {
        return teamService.findUsersByKeyword(id, queryString);
    }

    @PostMapping("/{teamId}/invite/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamInvite inviteMember(@LoginUser User user, @PathVariable Long teamId, @PathVariable Long userId) {
        return teamInviteService.createTeamInvite(user,userId, teamId);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Team> readTeams(@LoginUser User user) {
        return teamService.findTeamsByUser(user);
    }

    @PostMapping("/profile/{teamId}")
    public Team changeProfile(@LoginUser User user, @PathVariable Long teamId, @RequestPart MultipartFile file) {
        return teamService.changeProfile(teamService.findTeamById(teamId), file);
    }

    @ExceptionHandler(DuplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorEntity> handleDuplicationException(ErrorEntityException exception) {
        return Arrays.asList(exception.entity());
    }
}
