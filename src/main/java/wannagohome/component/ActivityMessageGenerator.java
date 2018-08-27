package wannagohome.component;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import wannagohome.domain.activity.AbstractActivity;
import wannagohome.domain.activity.Activity;
import wannagohome.domain.activity.ActivityDto;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityMessageGenerator {

    @Resource(name = "activityMessageSourceAccessor")
    private MessageSourceAccessor messageSourceAccessor;

    public List<ActivityDto> generateMessages(List<? extends AbstractActivity> activities) {
        return activities.stream()
                .map(this::generateMessage)
                .collect(Collectors.toList());
    }

    public ActivityDto generateMessage(Activity activity) {
        return ActivityDto.valueOf(messageSourceAccessor, activity);
    }
}
