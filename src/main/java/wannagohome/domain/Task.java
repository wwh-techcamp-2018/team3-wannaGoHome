package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "board_tasks_id")
    private Board board;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String title;

    @Transient
    private List<Card> cards;

//    @NotBlank
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    public Task(String title) {
        this.title = title;
        this.cards = new ArrayList<Card>();
    }

    public Task(String title, List<Card> cards) {
        this.title = title;
        this.cards = cards;
    }

    public Task(TaskDto taskDto) {
        this.title = taskDto.getTitle();
        this.cards = taskDto.getCards();
        this.author = taskDto.getAuthor();
        this.deleted = false;
    }

    @JsonIgnore
    public TaskDto getTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(title);
        taskDto.setCards(cards);
        return taskDto;
    }
}
