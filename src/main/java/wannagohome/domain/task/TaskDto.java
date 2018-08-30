package wannagohome.domain.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import wannagohome.domain.card.Card;
import wannagohome.domain.card.CardDto;
import wannagohome.domain.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TaskDto {

    private Long id;


    private User author;

    @NotBlank
    @Size(min = 1, max = 30)
    private String title;

    private List<CardDto> cards = new ArrayList<>();

    private Integer orderId;
    private boolean deleted;

    @JsonIgnore
    public List<Card> getRealCards() {
        return cards.stream().map(Card::new).collect(Collectors.toList());
    }

}
