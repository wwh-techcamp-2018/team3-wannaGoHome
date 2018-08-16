package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Task {

    private User author;
    private String title;
    private List<Card> cards;

    private boolean deleted;

    public Task() {}

    public Task(String title) {
        this.title = title;
        this.cards = new ArrayList<Card>();
    }

    public Task(String title, List<Card> cards) {
        this.title = title;
        this.cards = cards;
    }

    public TaskDto getTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(title);
        taskDto.setCards(cards);
        return taskDto;
    }
}
