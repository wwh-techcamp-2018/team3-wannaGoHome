package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.*;
import wannagohome.repository.ActivityRepository;
import wannagohome.repository.UserIncludedInTeamRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    public AbstractActivity create(AbstractActivity activity) {
        return activityRepository.save(activity);
    }

    public List<AbstractActivity> findRecentActivities(User user) {
        List<Team> teams = userIncludedInTeamRepository.findAllByUser(user).stream()
                .map(UserIncludedInTeam::getTeam)
                .collect(Collectors.toList());
        return null;
    }

    public List<AbstractActivity> findBoardActivities(Board board) {
//
        return null;
    }
}
