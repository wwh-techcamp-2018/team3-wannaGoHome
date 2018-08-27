package wannagohome.domain.board;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBoardDto {

    @NotBlank
    @Size(max = 20)
    private String title;

    @NotNull
    private Long teamId;

    @NotBlank
    private String color;
}
