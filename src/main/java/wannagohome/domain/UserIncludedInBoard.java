package wannagohome.domain;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Builder
public class UserIncludedInBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Enumerated(EnumType.ORDINAL)
    private UserPermission permission;
}
