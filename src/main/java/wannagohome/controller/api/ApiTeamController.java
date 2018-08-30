package wannagohome.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.board.BoardOfTeamDto;
import wannagohome.domain.error.ErrorEntity;
import wannagohome.domain.team.RemoveUserFromTeamDto;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamInvite;
import wannagohome.domain.team.TeamPermissionChangeDto;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserDto;
import wannagohome.domain.user.UserIncludedInTeam;
import wannagohome.exception.DuplicationException;
import wannagohome.exception.ErrorEntityException;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.TeamInviteService;
import wannagohome.service.TeamService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
@Api(value = "Team", description = "Team 관리 API")
public class ApiTeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamInviteService teamInviteService;

    @ApiOperation(value = "팀 생성하기")
    @ApiImplicitParam(name = "createTeam", value = "BoardOfTeamDto", required = true, paramType = "json")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BoardOfTeamDto create(@LoginUser User user, @RequestBody @Valid Team team) {
        return teamService.create(team, user);
    }

    @ApiOperation(value = "teamId로 팀 가져오기")
    @ApiImplicitParam(name = "readTeam", value = "Team", required = true, paramType = "json")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Team readTeam(@PathVariable Long id) {
        return teamService.findTeamById(id);
    }

    @ApiOperation(value = "team 삭제하기")
    @ApiImplicitParam(name = "deleteTeam", value = "삭제된 팀", required = true, paramType = "json")
    @DeleteMapping("/{id}")
    public Team deleteTeam(@LoginUser User user, @PathVariable Long id) {
        return teamService.deleteTeam(user, id);
    }

    @ApiOperation(value = "team 멤버 가져오기")
    @ApiImplicitParam(name = "teamMembers", value = "UserDto 리스트", required = true, paramType = "json")
    @GetMapping("/{id}/members")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> teamMembers(@PathVariable Long id) {
        return teamService.findByTeam(teamService.findTeamById(id));
    }

    @ApiOperation(value = "id로 팀의 멤버 가져오기")
    @ApiImplicitParam(name = "teamMember", value = "UserDto", required = true, paramType = "json")
    @GetMapping("/{id}/member")
    @ResponseStatus(HttpStatus.OK)
    public UserDto teamMember(@LoginUser User user, @PathVariable Long id) {
        return teamService.findByUserOfTeam(user, teamService.findTeamById(id));
    }

    @ApiOperation(value = "검색어로 팀의 멤버 찾기")
    @ApiImplicitParam(name = "searchResults", value = "UserDto 리스트", required = true, paramType = "json")
    @GetMapping("/{id}/search/{queryString}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> searchResults(@PathVariable Long id, @PathVariable String queryString) {
        return teamService.findUsersByKeyword(id, queryString);
    }

    @ApiOperation(value = "팀 멤버 초대하기")
    @ApiImplicitParam(name = "inviteMember", value = "TeamInvite", required = true, paramType = "json")
    @PostMapping("/{teamId}/invite/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public TeamInvite inviteMember(@LoginUser User user, @PathVariable Long teamId, @PathVariable Long userId) {
        return teamInviteService.createTeamInvite(user, userId, teamId);
    }

    @ApiOperation(value = "권한 변경하기")
    @ApiImplicitParam(name = "changePermission", value = "UserIncludedInTeam", required = true, paramType = "json")
    @PutMapping("/{teamId}/permission")
    @ResponseStatus(HttpStatus.OK)
    public UserIncludedInTeam changePermission(@LoginUser User user,
                                               @RequestBody TeamPermissionChangeDto permissionDto) {
        return teamService.changePermission(user, permissionDto);
    }

    @ApiOperation(value = "유저가 속한 팀 가져오기")
    @ApiImplicitParam(name = "readTeams", value = "Team 리스트", required = true, paramType = "json")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Team> readTeams(@LoginUser User user) {
        return teamService.findTeamsByUser(user);
    }

    @ApiOperation(value = "팀 프로필 변경하기")
    @ApiImplicitParam(name = "changeProfile", value = "Team", required = true, paramType = "json")
    @PostMapping("/profile/{teamId}")
    public Team changeProfile(@LoginUser User user, @PathVariable Long teamId, @RequestPart MultipartFile file) {
        return teamService.changeProfile(teamService.findTeamById(teamId), file);
    }

    @ApiOperation(value = "Team 에서 멤버 추방하기")
    @ApiImplicitParam(name = "removeUserFromTeam", value = "삭제된 user Dto", required = true, paramType = "json")
    @DeleteMapping("{teamId}/users")
    public UserDto removeUserFromTeam(@LoginUser User user, @Valid @RequestBody RemoveUserFromTeamDto removeUserFromTeamDto) {
        return teamService.removeUserFromTeam(user, removeUserFromTeamDto);
    }

    @ApiOperation(value = "에러 exception 처리하기")
    @ApiImplicitParam(name = "handleDuplicationException", value = "ErrorEntity 리스트", required = true, paramType = "json")
    @ExceptionHandler(DuplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorEntity> handleDuplicationException(ErrorEntityException exception) {
        return Arrays.asList(exception.entity());
    }


}
