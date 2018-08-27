package wannagohome.domain.card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardLabelDto {

    private Long id;
    @Enumerated(value = EnumType.STRING)
    private LabelColor color;
    private boolean checked;

    public static CardLabelDto valueOf(Label label, Card card) {
        return new CardLabelDto(label.getId(), label.getColor(), card.containsLabel(label));
    }
}
