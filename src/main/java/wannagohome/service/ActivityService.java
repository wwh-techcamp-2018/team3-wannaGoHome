package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.*;
import wannagohome.repository.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ActivityService {

    private final int MAX_MESSAGE_COUNT = 10;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMessageGenerator activityMessageGenerator;

    public AbstractActivity create(AbstractActivity activity) {
        return activityRepository.save(activity);
    }

    public ActivityDto findUserActivities(User user) {

        return generateActivityDto(
                activityRepository
                        .findFindFirst10ByActivityTypeInAndReceiverOrderByRegisteredDateDesc(
                                Arrays.asList(
                                        AbstractActivity.TEAM_ACTIVITY,
                                        AbstractActivity.BOARD_ACTIVITY
                                ),
                                user
                        )
        );
    }

    public ActivityDto findBoardActivities(User user, Board board) {
        return generateActivityDto(
                activityRepository
                .findFindFirst10ByActivityTypeInAndReceiverOrderByRegisteredDateDesc(
                        Arrays.asList(
                                AbstractActivity.TASK_ACTIVITY,
                                AbstractActivity.CARD_ACTIVITY
                        ),
                        user
                )
        );
    }

    private ActivityDto generateActivityDto(List<AbstractActivity> activities) {
        Collections.sort(activities);
        return new ActivityDto(
                activityMessageGenerator.generateMessages(activities.subList(0, activities.size() < MAX_MESSAGE_COUNT ? activities.size() : MAX_MESSAGE_COUNT))
        );
    }



}
