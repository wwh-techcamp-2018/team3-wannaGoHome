package wannagohome.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.task.Task;
import wannagohome.domain.task.TaskDto;
import wannagohome.domain.task.TaskOrderDto;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.UserIncludedInBoard;
import wannagohome.exception.UnAuthorizedException;

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
@AllArgsConstructor
public class Board {
    public static final String BOARD_HEADER_TOPIC_URL = "/topic/board/%d/header";

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

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "board")
    @OrderBy("order_id ASC")
    @Where(clause = "deleted = false")
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
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
        boardDto.setColor(color);
        List<TaskDto> taskDtoList = tasks.stream()
                .map(Task::getTaskDto)
                .collect(Collectors.toList());
        boardDto.setTasks(taskDtoList);
        return boardDto;
    }

    public Board addTask(Task task) {
        tasks.add(task);
        task.setOrderId(tasks.size());
        return this;
    }

    public Board reorderTasks(TaskOrderDto taskOrderDto) {
        if (taskOrderDto.getDestinationIndex() >= tasks.size()) {
            return this;
        }
        for (int i = 0; i < tasks.size(); ++i) {
            if (tasks.get(i).equalsId(taskOrderDto.getOriginId())) {
                Task movingTask = tasks.get(i);
                tasks.remove(i);
                tasks.add(taskOrderDto.getDestinationIndex(), movingTask);
                break;
            }
        }
        for (int i = 0; i < tasks.size(); ++i) {
            tasks.get(i).setOrderId(i);
        }
        return this;
    }

    public void delete(UserIncludedInBoard userIncludedInBoard) {
        if (!userIncludedInBoard.isAdmin()) {
            throw new UnAuthorizedException(ErrorType.UNAUTHORIZED, "보드를 삭제할 권한이 없습니다.");
        }
        deleted = true;
    }

    public void delete() {
        deleted = true;
    }

    public void rename(UserIncludedInBoard userIncludedInBoard, BoardHeaderDto dto) {
        if (!userIncludedInBoard.isAdmin()) {
            throw new UnAuthorizedException(ErrorType.UNAUTHORIZED, "보드명을 수정할 권한이 없습니다.");
        }
        this.title = dto.getBoardTitle();
    }
}
