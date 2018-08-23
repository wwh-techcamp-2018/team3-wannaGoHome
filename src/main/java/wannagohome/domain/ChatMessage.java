package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault(value="0")
    private Long messageOrder;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name="board_id", nullable=false)
    private Board board;

    @Column(name = "message_created", updatable = false)
    private Date messageCreated;

    @NotBlank
    @Lob
    @Column(nullable = false)
    private String text;

    public ChatMessage(ChatMessageDto chatMessageDto) {
        this.text = chatMessageDto.getText();
        this.messageCreated = new Date();
    }

    @JsonIgnore
    public ChatMessageDto getChatMessageDto() {
        ChatMessageDto messageDto = new ChatMessageDto();
        messageDto.setAuthor(author.getUserDto());
        messageDto.setMessageOrder(messageOrder);
        messageDto.setText(text);
        messageDto.setMessageCreated(messageCreated);

        return messageDto;
    }
}
