package wannagohome.domain;


import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
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
