package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskDto {

    private Long id;


    private User author;

    @NotBlank
    @Size(min = 1, max = 30)
    private String title;

    private List<CardDto> cards = new ArrayList<>();

    private boolean deleted;

    @JsonIgnore
    public List<Card> getRealCards() {
        List<Card> realCards = new ArrayList<>();
        for (CardDto card : cards) {
            realCards.add(new Card(card));
        }
        return realCards;
    }

}
