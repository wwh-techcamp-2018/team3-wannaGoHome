package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wannagohome.domain.ActivityDto;
import wannagohome.domain.RequestActivityDto;
import wannagohome.domain.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.ActivityService;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ApiActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/fetch")
    public List<ActivityDto> fetch(@LoginUser User user, @RequestBody RequestActivityDto requestActivityDto) {
        return activityService.fetchActivity(user, requestActivityDto.getRegisteredDate());
    }
}
