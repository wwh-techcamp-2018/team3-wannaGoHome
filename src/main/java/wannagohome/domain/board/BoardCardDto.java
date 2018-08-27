package wannagohome.domain.board;

import lombok.*;
import wannagohome.domain.team.Team;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCardDto {
    private Long id;

    private String title;

    private Team team;

    private Color color;


    public static BoardCardDto valueOf(Board board) {
        BoardCardDto boardCardDto = BoardCardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .team(board.getTeam())
                .color(board.getColor())
                .build();
        return boardCardDto;

    }
}
