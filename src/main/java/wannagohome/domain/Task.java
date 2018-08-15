package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Task {

    private User author;
    private String title;
    private List<Card> cards;

    private boolean deleted;
}
