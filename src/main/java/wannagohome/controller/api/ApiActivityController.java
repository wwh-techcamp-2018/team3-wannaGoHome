package wannagohome.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wannagohome.domain.activity.ActivityDto;
import wannagohome.domain.activity.RequestActivityDto;
import wannagohome.domain.user.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.ActivityService;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@Api(value = "Activity", description = "acticity 기록 API")
public class ApiActivityController {

    @Autowired
    private ActivityService activityService;

    @ApiOperation(value = "해당 유저의 activity 불러오기")
    @ApiImplicitParam(name = "activity", value = "activity Dto 리스트", required = true, paramType = "json")
    @PostMapping("/fetch")
    public List<ActivityDto> fetch(@LoginUser User user, @RequestBody RequestActivityDto requestActivityDto) {
        return activityService.fetchActivity(user, requestActivityDto.getRegisteredDate());
    }
}
