package wannagohome.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.team.TeamInvitationDto;
import wannagohome.domain.team.TeamInvite;
import wannagohome.domain.user.*;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.ActivityService;
import wannagohome.service.TeamInviteService;
import wannagohome.service.TeamService;
import wannagohome.service.UserService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Api(value = "User", description = "User 관리 API")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private TeamInviteService teamInviteService;


    @ApiOperation(value = "회원가입 요청")
    @ApiImplicitParam(name = "signUp", value = "User", required = true, paramType = "json")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User signUp(@RequestBody @Valid SignUpDto dto) {
        return userService.signUp(dto);
    }

    @ApiOperation(value = "로그인 요청")
    @ApiImplicitParam(name = "signIn", value = "User", required = true, paramType = "json")
    @PostMapping("/signin")
    public User signIn(HttpSession session, @RequestBody @Valid SignInDto dto) {
        User user = userService.signIn(dto);
        SessionUtil.setUserSession(session, user);
        return user;
    }


    @ApiOperation(value = "로그아웃 요청")
    @ApiImplicitParam(name = "logout", required = true, paramType = "json")
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        SessionUtil.removeUserSession(session);
    }

    @ApiOperation(value = "세션 유저 가져오기")
    @ApiImplicitParam(name = "getUserDtoInSession", value = "UserDto", required = true, paramType = "json")
    @GetMapping("/userId")
    public UserDto getUserDtoInSession(HttpSession session) {
        return SessionUtil.getUserSession(session).getUserDto();
    }

    @ApiOperation(value = "mypage 정보 가져오기")
    @ApiImplicitParam(name = "profile", value = "MyPageDto", required = true, paramType = "json")
    @GetMapping("/profile")
    public MyPageDto profile(@LoginUser User user) {
        return MyPageDto.valueOf(
                UserDto.valueOf(user)
                , teamService.findTeamsByUser(user)
                , activityService.findUserActivities(user)
                , teamInviteService.findAllByMember(user)
        );
    }

    @ApiOperation(value = "profile 초기화")
    @ApiImplicitParam(name = "initializeProfile", value = "UserDto", required = true, paramType = "json")
    @PostMapping("/profile/init")
    public UserDto initializeProfile(@LoginUser User user) {
        return UserDto.valueOf(userService.initializeProfile(user));
    }

    @ApiOperation(value = "profile 변경하기")
    @ApiImplicitParam(name = "changeProfile", value = "UserDto", required = true, paramType = "json")
    @PostMapping("/profile")
    public UserDto changeProfile(@LoginUser User user, @RequestPart MultipartFile file) {
        return UserDto.valueOf(userService.changeProfile(user, file));
    }

    @ApiOperation(value = "user 이름 변경하기")
    @ApiImplicitParam(name = "changeName", value = "UserDto", required = true, paramType = "json")
    @PutMapping("/profile")
    public UserDto changeName(@LoginUser User user, @Valid @RequestBody UserDto userDto) {
        user.setName(userDto.getName());
        return UserDto.valueOf(userService.save(user));
    }

    @ApiOperation(value = "팀 초대하기")
    @ApiImplicitParam(name = "processTeamInvitation", value = "TeamInvite", required = true, paramType = "json")
    @PostMapping("/invitation")
    public TeamInvite processTeamInvitation(@LoginUser User user, @RequestBody TeamInvitationDto invitationDto) {
        return userService.processTeamInvitation(user, invitationDto);
    }
}
