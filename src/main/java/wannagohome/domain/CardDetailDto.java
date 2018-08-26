package wannagohome.domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailDto {
    private Long id;
    private Date endDate;
    private List<Label> labels;

    private Long userId;

    private String description;
}
