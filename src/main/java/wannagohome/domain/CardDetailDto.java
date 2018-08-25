package wannagohome.domain;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDetailDto {

    private Long userId;

    private String description;
}
