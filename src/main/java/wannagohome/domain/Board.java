package wannagohome.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(length = 20, nullable = false)
    private String title;

    @ManyToOne
    private Team team;

    @Transient
    private List<Task> tasks;
//    private List<Activity> activities;
    @Enumerated(EnumType.ORDINAL)
    private Color color;

    @NotBlank
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    public BoardDto getBoardDto() {
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle(title);
        List<TaskDto> taskDtoList = new ArrayList<TaskDto>();
        for (Task task : tasks) {
            taskDtoList.add(task.getTaskDto());
        }
        boardDto.setTasks(taskDtoList);
        return boardDto;
    }
}
