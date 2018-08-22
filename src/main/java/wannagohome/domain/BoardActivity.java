package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@Entity
@NoArgsConstructor
@DiscriminatorValue("BoardActivity")
public class BoardActivity extends AbstractActivity {

    @Getter
    @ManyToOne
    private Board board;

    @Override
    public Object[] getArguments() {
        return new Object[]{board.getTitle(), source.getName()};
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
