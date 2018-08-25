package wannagohome.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardOrderDto {
    private Long originId;
    private int destinationIndex;
}
