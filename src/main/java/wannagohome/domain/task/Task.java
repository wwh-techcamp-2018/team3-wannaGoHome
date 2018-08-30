package wannagohome.domain.task;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import wannagohome.domain.board.Board;
import wannagohome.domain.card.Card;
import wannagohome.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable=false)
    private Board board;

    @NotBlank
    @Size(min=1, max = 30)
    @Column(length = 30, nullable = false)
    private String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @OrderBy("order_id ASC")
    @Where(clause = "deleted = false")
    private List<Card> cards;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    @Column(name="order_id", nullable = false)
    @ColumnDefault(value="0")
    private Integer orderId;

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
        this.cards = taskDto.getRealCards();
        this.author = taskDto.getAuthor();
        this.deleted = false;
    }

    @JsonIgnore
    public TaskDto getTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(id);
        taskDto.setTitle(title);
        taskDto.setCards(
                cards.stream()
                        .filter(card -> !card.isDeleted())
                        .map(Card::getCardDto)
                        .collect(Collectors.toList())
        );
        taskDto.setOrderId(orderId);
        return taskDto;
    }

    public Task addCard(Card card) {
        this.cards.add(card);
        if(card.getTask() != this) {
            card.setOrderId(this.cards.size());
        }
        card.setTask(this);
        return this;
    }

    public boolean equalsId(Long id) {
        return this.id.equals(id);
    }
}
