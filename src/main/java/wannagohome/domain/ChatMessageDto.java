package wannagohome.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ChatMessageDto {

    private UserDto author;
    private String text;
    private Long messageOrder;
    private boolean sameAuthor;

}
