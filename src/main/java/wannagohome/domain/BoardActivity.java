package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Builder
@Entity
@DiscriminatorValue("BoardActivity")
@NoArgsConstructor
@AllArgsConstructor
public class BoardActivity extends AbstractActivity {


    @Getter
    @ManyToOne
    private Board board;

    @Override
    public Object[] getArguments() {
        return new Object[]{board.getTitle()};
    }


    public static BoardActivity valueOf(User user, Board board, ActivityType activityType) {
        BoardActivity activity = new BoardActivity();
        activity.source = user;
        activity.board = board;
        activity.type = activityType;
        return activity;
    }

    public Team getTeam() {
        return board.getTeam();
    }

    @Override
    public String getTopicUrl() {
        return "/topic/activity/team/" + board.getTeam().getId();
    }

    @Override
    public String getSubscribeTopicUrl() {
        if(ActivityType.BOARD_CREATE == type) {
            return "/topic/activity/board/" + board.getId();
        }
        return super.getSubscribeTopicUrl();
    }
}
