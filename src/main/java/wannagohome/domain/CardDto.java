package wannagohome.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class CardDto {

    private Long id;
    private String title;
    private Date createDate;
    private User author;
}
