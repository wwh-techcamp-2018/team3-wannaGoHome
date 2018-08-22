package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wannagohome.domain.ActivityInitDto;
import wannagohome.domain.User;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private ActivityService activityService;

    @Resource(name = "biDirectionEncoder")
    private PasswordEncoder biDirectionDecoder;

    public ActivityInitDto initNotification(User user) {
        List<String> messages = activityService.findUserActivities(user);
        return new ActivityInitDto(biDirectionDecoder.encode(user.getEmail()), messages);
    }
}
