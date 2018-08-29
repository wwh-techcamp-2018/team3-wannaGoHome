package wannagohome.domain.file;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.card.Card;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String originFileName;

    @NotNull
    @Lob
    private String link;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "CARD_ID")
    private Card card;

    public Attachment(Card card, String originFileName, String link) {
        this.card = card;
        this.originFileName = originFileName;
        this.link = link;
    }

}
