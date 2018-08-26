package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class CardDetailDto {
    private Long id;
    private Date endDate;
    private List<User> assignees;
    private List<Label> labels;
}
