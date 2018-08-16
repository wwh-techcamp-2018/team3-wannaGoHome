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
