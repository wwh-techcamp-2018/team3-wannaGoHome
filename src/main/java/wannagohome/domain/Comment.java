package wannagohome.domain;


import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User author;

    @ManyToOne
    @JoinColumn(name = "CARD_ID")
    private Card card;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;
}
