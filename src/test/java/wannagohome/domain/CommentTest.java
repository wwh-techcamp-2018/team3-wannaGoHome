package wannagohome.domain;

import org.junit.Before;
import org.junit.Test;
import wannagohome.domain.card.Card;
import wannagohome.domain.card.Comment;
import wannagohome.domain.card.CommentDto;
import wannagohome.domain.user.User;
import wannagohome.exception.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    private Comment comment;

    private User author;

    private User other;

    @Before
    public void setUp() throws Exception {
        author = User.builder()
                .email("songintae@woowahan.com")
                .password("passowrd1")
                .name("kookooku")
                .build();

        other = User.builder()
                .email("jhyang1235@woowahan.com")
                .password("seohyeon")
                .name("gogo")
                .build();

        comment = Comment.valueOf(
                new CommentDto("comment contents"),
                author,
                new Card()
        );
    }

    @Test
    public void delete() {
        comment.delete(author);
        assertThat(comment.isDeleted()).isTrue();
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_권한없음() {
        comment.delete(other);
    }
}