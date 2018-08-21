package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode
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

    @Basic(optional = false)
    @Column(name = "message_created", updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageCreated;

    @NotBlank
    @Column(columnDefinition = "BLOB", nullable = false)
    private String text;

    public ChatMessage(ChatMessageDto chatMessageDto) {
        this.text = chatMessageDto.getText();
        this.messageCreated = new Date();
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", messageOrder=" + messageOrder +
                ", author=" + author +
                ", board=" + board +
                ", messageCreated=" + messageCreated +
                ", text='" + text + '\'' +
                '}';
    }

    @JsonIgnore
    public ChatMessageDto getChatMessageDto(User currentUser) {
        ChatMessageDto messageDto = new ChatMessageDto();
        messageDto.setAuthor(author.getUserDto());
        messageDto.setMessageOrder(messageOrder);
        messageDto.setText(text);

        return messageDto;
    }
}
