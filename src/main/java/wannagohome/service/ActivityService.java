package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.*;
import wannagohome.event.ActivityEventHandler;
import wannagohome.repository.ActivityRepository;
import wannagohome.util.DateUtil;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMessageGenerator activityMessageGenerator;

    @Resource(name = "biDirectionEncoder")
    private PasswordEncoder biDirectionDecoder;

    @Autowired
    private ActivityEventHandler activityEventHandler;

    public AbstractActivity create(AbstractActivity activity) {
        return activityRepository.save(activity);
    }

    public List<ActivityDto> findUserActivities(User user) {

        return generateActivityDto(
                activityRepository.findFirst10ByReceiverOrderByRegisteredDateDesc(user)
        );
    }

    private List<ActivityDto> generateActivityDto(List<AbstractActivity> activities) {
        return activityMessageGenerator.generateMessages(activities);
    }


    public ActivityInitDto initNotification(User user) {
        return new ActivityInitDto(
                biDirectionDecoder.encode(user.getEmail()),
                findUserActivities(user)
        );
    }

    public List<ActivityDto> fetchActivity(User user, String registeredDate) {
        return generateActivityDto(
                activityRepository.findFirst10ByReceiverAndRegisteredDateLessThanOrderByRegisteredDateDesc(
                        user, DateUtil.getDate(registeredDate)
                )
        );
    }

    public void sendPreviousActivities(User user, RequestActivityDto requestActivityDto) {
        activityEventHandler.sendPersonalMessage(
                user,
                activityRepository.findFirst10ByReceiverAndRegisteredDateLessThanOrderByRegisteredDateDesc(
                        user,
                        DateUtil.getDate(requestActivityDto.getRegisteredDate())
                )
        );
    }
}
