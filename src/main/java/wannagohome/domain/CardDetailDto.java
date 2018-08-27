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
    private Date createDate;
    private List<Label> labels;

    private Long userId;

    private String cardTitle;

    private String taskTitle;

    private List<Comment> comments;

    private String description;

    public CardDetailDto(String cardTitle, String taskTitle, String description, List<Comment> comments) {
        this.cardTitle = cardTitle;
        this.taskTitle = taskTitle;
        this.description = description;
        this.comments = comments;
    }
}
