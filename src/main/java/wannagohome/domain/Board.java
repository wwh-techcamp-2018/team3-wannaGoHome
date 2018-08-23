package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    @Getter
    private String title;

    @ManyToOne
    private Team team;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "board")
    @OrderBy("order_id ASC")
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
    @Getter
    private Color color;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    public Board() {
        tasks = new ArrayList<>();
    }

    @JsonIgnore
    public BoardDto getBoardDto() {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(id);
        boardDto.setTitle(title);
        List<TaskDto> taskDtoList =  tasks.stream().map(task -> task.getTaskDto()).collect(Collectors.toList());
        boardDto.setTasks(taskDtoList);
        return boardDto;
    }

    public Board addTask(Task task) {
        tasks.add(task);
        task.setOrderId(tasks.size());
        return this;
    }

    public Board reorderTasks(TaskOrderDto taskOrderDto) {
        if(taskOrderDto.getDestinationIndex() >= tasks.size()) {
            return this;
        }

        for(int i = 0; i < tasks.size(); ++i) {
            if(tasks.get(i).equalsId(taskOrderDto.getOriginId())) {
                Task movingTask = tasks.get(i);
                tasks.remove(i);
                tasks.add(taskOrderDto.getDestinationIndex(), movingTask);
                break;
            }
        }

        for(int i = 0; i < tasks.size(); ++i) {
            tasks.get(i).setOrderId(i);
        }

        return this;
    }

}
