package wannagohome.domain;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


@Getter
@Entity
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

//    @ManyToMany
//    @JoinTable(
//            name = "CARD_ASSIGNEE",
//            joinColumns = @JoinColumn(name = "CARD_ID"),
//            inverseJoinColumns = @JoinColumn(name = "USER_ID")
//    )
//    private List<User> assignees;
//
//    @ManyToMany
//    @JoinTable(
//            name = "CARD_LABEL",
//            joinColumns = @JoinColumn(name = "CARD_ID"),
//            inverseJoinColumns = @JoinColumn(name = "LABEL_ID")
//    )
//    private List<Label> labels;


    private Date createDate;

    private Date endDate;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    // TODO: 2018. 8. 20. 쓰는사람이 만들기.
    @Transient
    private Integer orderId;
}