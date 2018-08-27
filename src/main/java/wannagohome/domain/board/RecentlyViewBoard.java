package wannagohome.domain.board;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.board.Board;
import wannagohome.domain.user.User;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentlyViewBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Getter
    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;
}
