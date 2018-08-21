package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotBlank
    private Long order;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User author;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="board_id", nullable=false)
    private Board board;

    @Basic(optional = false)
    @Column(name = "LastTouched", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageCreated;

    @NotBlank
    @Column(columnDefinition = "BLOB", nullable = false)
    private String text;

    public ChatMessage(ChatMessageDto chatMessageDto) {
        this.text = chatMessageDto.getText();
    }

    @JsonIgnore
    public ChatMessageDto getChatMessageDto() {
        ChatMessageDto messageDto = new ChatMessageDto();
        messageDto.setAuthor(author);
        messageDto.setOrder(order);
        messageDto.setText(text);
        return messageDto;
    }
}
