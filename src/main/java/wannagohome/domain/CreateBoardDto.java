package wannagohome.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
public class CreateBoardDto {

    @NotBlank
    @Size(max = 20)
    private String title;

    @NotNull
    private Long teamId;

    @NotBlank
    private String color;
}
