package wannagohome.domain.card;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.user.User;
import wannagohome.exception.UnAuthorizedException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User author;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "CARD_ID")
    private Card card;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean deleted;

    private Comment(String contents, User author, Card card) {
        this.contents = contents;
        this.author = author;
        this.card = card;
    }

    public static Comment valueOf(CommentDto dto, User author, Card card) {
        return new Comment(dto.getContents(), author, card);
    }

    private boolean deletable(User user) {
        // TODO: 지울 수 있는 조건이 더 존재하는가?
        return author.equals(user);
    }

    public void delete(User user) {
        if (!deletable(user)) {
            throw new UnAuthorizedException(ErrorType.UNAUTHORIZED, "댓글은 쓴 사람만 지울 수 있습니다.");
        }
        deleted = true;
    }
}
