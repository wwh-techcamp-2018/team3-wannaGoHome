package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityInitDto {

    private List<String> topics;

    private List<String> messages;

    private String code;
}
