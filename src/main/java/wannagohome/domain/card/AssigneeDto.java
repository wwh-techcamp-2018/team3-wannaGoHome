package wannagohome.domain.card;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.user.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssigneeDto {

    private Long assigneeId;
    private String name;
    private String email;
    private boolean assigned;

    public static AssigneeDto valueOf(User user, Card card) {
        return new AssigneeDto(user.getId(), user.getName(), user.getEmail(), card.containsAssignee(user));
    }
}
