package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    @CreatedDate
    private Date createDate;

    private Date endDate;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    // TODO: 2018. 8. 20. 쓰는사람이 만들기.
    private Integer orderId;

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
        return this.id == id;
    }

}