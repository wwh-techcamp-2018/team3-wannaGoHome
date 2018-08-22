package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.ActivityDto;
import wannagohome.domain.User;
import wannagohome.repository.ActivityRepository;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMessageGenerator activityMessageGenerator;

    public AbstractActivity create(AbstractActivity activity) {
        return activityRepository.save(activity);
    }

    public List<ActivityDto> findUserActivities(User user) {

        return generateActivityDto(
                activityRepository.findFindFirst10ByReceiverOrderByRegisteredDateDesc(user)
        );
    }

    private List<ActivityDto> generateActivityDto(List<AbstractActivity> activities) {
        return activityMessageGenerator.generateMessages(activities);
    }
}
