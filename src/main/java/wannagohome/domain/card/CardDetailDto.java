package wannagohome.domain.card;

import lombok.*;
import wannagohome.domain.file.Attachment;
import wannagohome.domain.user.User;

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
    private List<Label> allLabels;
    private List<User> assignees;

    private Long userId;

    private String cardTitle;

    private String taskTitle;

    private List<Comment> comments;

    private List<Attachment> attachments;
    private String description;

    public static CardDetailDto valueOf(Card card, List<Label> allLabels) {
        return CardDetailDto.builder()
                .cardTitle(card.getTitle())
                .taskTitle(card.getTask().getTitle())
                .description(card.getDescription())
                .comments(card.getComments())
                .labels(card.getLabels())
                .allLabels(allLabels)
                .assignees(card.getAssignees())
                .endDate(card.getEndDate())
                .attachments(card.getAttachments())
                .build();
    }
}
