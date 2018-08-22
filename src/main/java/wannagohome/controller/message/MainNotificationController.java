package wannagohome.controller.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import wannagohome.domain.ActivityInitDto;
import wannagohome.service.NotificationService;
import wannagohome.util.SessionUtil;

@Controller
public class MainNotificationController {
    private static final Logger log = LoggerFactory.getLogger(MainNotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/activity/init")
    @SendTo("/topic/activity/init")
    public ActivityInitDto initNotification(SimpMessageHeaderAccessor headerAccessor) {
        return notificationService.initNotification(SessionUtil.getUserSession(headerAccessor));
    }
}
