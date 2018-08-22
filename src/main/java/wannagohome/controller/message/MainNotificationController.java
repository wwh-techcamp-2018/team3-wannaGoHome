package wannagohome.controller.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import wannagohome.domain.ActivityDto;
import wannagohome.interceptor.HttpHandshakeInterceptor;
import wannagohome.service.NotificationService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class MainNotificationController {
    private static final Logger log = LoggerFactory.getLogger(MainNotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/activity/init")
    @SendTo("/topic/activity/init")
    public ActivityDto initNotification(SimpMessageHeaderAccessor headerAccessor) {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION_ID);
        return notificationService.initNotification(SessionUtil.getUserSession(session));
    }
}
