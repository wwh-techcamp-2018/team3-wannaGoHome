package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class TaskDto {

    private Long id;


    private User author;

    @NotBlank
    @Size(min = 1, max = 30)
    private String title;
    private List<Card> cards;


    private boolean deleted;

}
