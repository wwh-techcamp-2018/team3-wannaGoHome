package wannagohome.controller.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import wannagohome.domain.activity.ActivityInitDto;
import wannagohome.domain.activity.RequestActivityDto;
import wannagohome.service.ActivityService;
import wannagohome.util.SessionUtil;

@Controller
public class MainNotificationController {
    @Autowired
    private ActivityService activityService;

    @MessageMapping("/activity/init")
    @SendTo("/topic/activity/init")
    public ActivityInitDto initNotification(SimpMessageHeaderAccessor headerAccessor) {
        return activityService.initNotification(SessionUtil.getUserSession(headerAccessor));
    }


    @MessageMapping("/activity/fetch")
    public String fetchActivities(SimpMessageHeaderAccessor headerAccessor, RequestActivityDto requestActivityDto) {
        activityService.sendPreviousActivities(SessionUtil.getUserSession(headerAccessor), requestActivityDto);
        return "";
    }
}
