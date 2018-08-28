package wannagohome.domain.card;

import lombok.*;
import wannagohome.domain.user.User;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private Long id;
    private String title;
    private Date createDate;
    private User author;
    private List<Label> labels;
    private Date dueDate;
}
