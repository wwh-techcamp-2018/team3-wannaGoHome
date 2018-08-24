package wannagohome.domain;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@DiscriminatorValue("BoardActivity")
public class BoardActivity extends AbstractActivity {

    @ManyToOne
    private Board board;

    @Override
    public Object[] getArguments() {
        return new Object[]{
                source.getName(),
                getTeam().getName(),
                getBoard().getTitle()
        };
    }

    private BoardActivity(User source, Board board, ActivityType activityType) {
        this.source = source;
        this.board = board;
        this.type = activityType;
    }

    public static BoardActivity valueOf(User source, Board board, ActivityType activityType) {
        return new BoardActivity(source, board, activityType);
    }

    @Override
    public Team getTeam() {
        return board.getTeam();
    }

    @Override
    public Board getBoard() {
        return board;
    }


}
