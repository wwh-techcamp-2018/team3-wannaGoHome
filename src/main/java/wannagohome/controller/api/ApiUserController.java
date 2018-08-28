package wannagohome.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.user.*;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.ActivityService;
import wannagohome.service.TeamService;
import wannagohome.service.UserService;
import wannagohome.service.file.ImageUploadService;
import wannagohome.service.file.UploadService;
import wannagohome.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ActivityService activityService;

    @Resource(name = "imageUploadService")
    private UploadService uploadService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User signUp(@RequestBody @Valid SignUpDto dto) {
        return userService.signUp(dto);
    }

    @PostMapping("/signin")
    public User signIn(HttpSession session, @RequestBody @Valid SignInDto dto) {
        User user = userService.signIn(dto);
        SessionUtil.setUserSession(session, user);
        return user;
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        SessionUtil.removeUserSession(session);
    }

    @GetMapping("/userId")
    public UserDto getUserDtoInSession(HttpSession session) {
        return SessionUtil.getUserSession(session).getUserDto();
    }

    @GetMapping("/profile")
    public MyPageDto profile(@LoginUser User user) {
        return MyPageDto.valueOf(
                UserDto.valueOf(user)
                , teamService.findTeamsByUser(user)
                , activityService.findUserActivities(user)
        );
    }

    @PostMapping("/profile/init")
    public UserDto initializeProfile(@LoginUser User user) {
        return UserDto.valueOf(userService.initializeProfile(user));
    }

    @PostMapping("/profile")
    public UserDto changeProfile(@LoginUser User user, @RequestPart MultipartFile file){
        user.setProfile(uploadService.fileUpload(file));
        return UserDto.valueOf(userService.save(user));
    }

    @PutMapping("/profile")
    public UserDto changeName(@LoginUser User user, @Valid @RequestBody UserDto userDto){
        user.setName(userDto.getName());
        return UserDto.valueOf(userService.save(user));
    }

}
