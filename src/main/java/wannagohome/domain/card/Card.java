package wannagohome.domain.card;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.task.Task;
import wannagohome.domain.team.Team;
import wannagohome.domain.board.Board;
import wannagohome.domain.user.User;
import wannagohome.exception.BadRequestException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User author;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String title;

    @Size(max = 255)
    private String description;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToMany
    @JoinTable(
            name = "CARD_ASSIGNEE",
            joinColumns = @JoinColumn(name = "CARD_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    private List<User> assignees;

    @ManyToMany
    @JoinTable(
            name = "CARD_LABEL",
            joinColumns = @JoinColumn(name = "CARD_ID"),
            inverseJoinColumns = @JoinColumn(name = "LABEL_ID")
    )
    private List<Label> labels;

    @JsonManagedReference
    @OneToMany(mappedBy = "card")
    private List<Comment> comments;

    @CreatedDate
    private Date createDate;

    private Date endDate;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    private Integer orderId;

    @JsonIgnore
    public Board getBoard() {
        return task.getBoard();
    }

    @JsonIgnore
    public Team getTeam() {
        return getBoard().getTeam();
    }

    public Card(CardDto cardDto) {
        this.title = cardDto.getTitle();
        this.author = cardDto.getAuthor();
        this.createDate = cardDto.getCreateDate();
    }

    public CardDto getCardDto() {
        CardDto cardDto = new CardDto();
        cardDto.setId(id);
        cardDto.setAuthor(author);
        cardDto.setCreateDate(createDate);
        cardDto.setTitle(title);
        return  cardDto;
    }

    public boolean equalsId(Long id) {
        return this.id.equals(id);
    }

    public void addAssignee(User assignee) {
        if (containsAssignee(assignee)) {
            throw new BadRequestException(ErrorType.CARD_ASSIGN_ALREADY_EXIST, "이미 존재하는 유저입니다.");
        }
        assignees.add(assignee);
    }

    public void dischargeAssignee(User assignee) {
        if (!containsAssignee(assignee)) {
            throw new BadRequestException(ErrorType.CARD_ASSIGN_NOT_EXIST, "해당 유저는 카드에 존재하지 않습니다.");
        }
        assignees.remove(assignee);
    }

    public void updateDescription(CardDetailDto dto) {
        this.description = dto.getDescription();
    }

    public boolean containsAssignee(User assignee) {
        return assignees.contains(assignee);
    }

    public boolean existDueDate() {
        return (!Objects.isNull(this.endDate));
    }

    public boolean containsLabel(Label label) {
        return labels.contains(label);
    }

    public void removeLabel(Label getLabel) {
        if(!containsLabel(getLabel)) {
            throw new BadRequestException(ErrorType.CARD_LABEL_NOT_EXIST, "해당 라벨은 카드에 존재하지 않습니다.");
        }
        labels.remove(getLabel);
    }
}
