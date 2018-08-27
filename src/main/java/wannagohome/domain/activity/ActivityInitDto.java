package wannagohome.domain.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityInitDto {
    private String topic;
    private List<ActivityDto> messages;
}
