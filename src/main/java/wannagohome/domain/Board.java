package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Getter
    private String title;

    @ManyToOne
    private Team team;

    @OneToMany(mappedBy = "board")
    private List<Task> tasks = new ArrayList<>();
//    private List<Activity> activities;

    @Enumerated(EnumType.ORDINAL)
    @Getter
    private Color color;

    @NotBlank
    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    @JsonIgnore
    public BoardDto getBoardDto() {

        BoardDto boardDto = new BoardDto();
        boardDto.setId(id);
        boardDto.setTitle(title);
        List<TaskDto> taskDtoList = new ArrayList<TaskDto>();

        System.out.println("Liable tasks");
        for (Task task : tasks) {
            taskDtoList.add(task.getTaskDto());
        }


        boardDto.setTasks(taskDtoList);
        return boardDto;
    }

    private void initializeTasks() {
        if(tasks == null) {
            tasks = new ArrayList<Task>();
        }
    }

    public Board addTask(Task task) {
        tasks.add(task);
        System.out.println(tasks.size());
        return this;
    }

    public Board reorderTasks(TaskOrderDto taskOrderDto) {
        for(int i = 0; i < tasks.size(); ++i) {
            if(i == taskOrderDto.getOriginIndex()) {
                Task movingTask = tasks.get(taskOrderDto.getOriginIndex());
                tasks.remove(taskOrderDto.getOriginIndex());
                tasks.add(taskOrderDto.getDestinationIndex(), movingTask);
                break;
            }
        }
        return this;
    }

}
